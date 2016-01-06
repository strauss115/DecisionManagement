package at.jku.se.decisiondocu.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import at.jku.se.decisiondocu.R;
import at.jku.se.decisiondocu.beans.adapters.ChatAdapter;
import at.jku.se.decisiondocu.chat.ChatClient;
import at.jku.se.decisiondocu.chat.ChatInterface;
import at.jku.se.decisiondocu.login.SaveSharedPreference;
import at.jku.se.decisiondocu.restclient.RestClient;
import at.jku.se.decisiondocu.restclient.RestHelper;
import at.jku.se.decisiondocu.restclient.client.model.NodeInterface;

import static android.os.SystemClock.sleep;

/**
 * Created by martin on 03.01.16.
 * <p/>
 * Activity that represents the client side chat functionality.
 * It consists of a listview with chat messages, an autoCompleteTextView and a Button
 */
@EActivity(R.layout.activity_chat)
public class ChatActivity extends AppCompatActivity implements ChatInterface {

    private static final int RESULT_TAKE_IMAGE = 0;

    protected ProgressDialog mDialog;
    private ChatClient mChatClient;

    // extra attributes for intent builder

    @Extra
    long dec_node_id;

    @Extra
    String usr_token;

    @Extra
    String DecisionName;

    @Extra
    String IPAddress;

    @Extra
    int Port;

    @ViewById(R.id.list)
    ListView mList;

    @ViewById(R.id.autoCompleteTextView1)
    AutoCompleteTextView editText;

    @Bean
    ChatAdapter mAdapter;

    /**
     * Send Button Click Listener
     */
    @Click(R.id.send_button)
    void OnBtnClick() {
        String message = editText.getText().toString();
        if (isMsgValid(message)) {
            sendMessage(message);
            editText.setText("");
        }
    }

    @Click(R.id.chat_take_picture)
    void PictureBtnClick() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, RESULT_TAKE_IMAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.chat_show_node)
    void ShowNodeClick() {
        new SearchNodeDetailsActivity_.IntentBuilder_(this)
                .decisionId(dec_node_id)
                .start();
    }

    @OnActivityResult(RESULT_TAKE_IMAGE)
    void PictureBtnResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if (imageBitmap != null) {
                try {
                    if (RestClient.saveDocument(imageBitmap, dec_node_id)) {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Background
    public void load() {
        showDialog();
        Map<String, String> map = RestClient.getRelationshipStrings();
        update(map);
    }

    @UiThread
    void update(Map<String, String> map) {
        if (map != null) {

            // generate possible hashtags

            Collection<String> collection = map.values();
            String[] values = new String[collection.size()];

            int i = 0;
            for (Iterator<String> it = collection.iterator(); it.hasNext(); ) {
                String s = it.next();
                values[i++] = "#" + s;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, values);
            editText.setAdapter(adapter);
            editText.setThreshold(1);
        }
        setTitle(DecisionName);
        mList.setAdapter(mAdapter);

        dismissDialog();

        // connect to the server
        new connectTask(this).execute("");
    }

    @UiThread
    void showDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(this);
            mDialog.setMessage("please wait...");
            mDialog.show();
        }
    }

    @UiThread
    void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @AfterViews
    void init() {
        load();
        mAdapter.setChatInterface(this);
    }

    private boolean isMsgValid(String msg) {
        return msg.length() > 0;
    }

    private boolean sendMessage(String message) {
        Log.d("sending", message);
        if (mChatClient != null) {
            mChatClient.sendMessage(message);
            return true;
        }
        return false;
    }

    /**
     * Async Task for connecting to the chatserver.
     * Once the connection is established, client and server can exchange messages over the socket.
     */
    public class connectTask extends AsyncTask<String, String, ChatClient> {

        private ChatInterface chatInterface;

        public connectTask(ChatInterface chatInterface) {
            this.chatInterface = chatInterface;
        }

        @Override
        protected ChatClient doInBackground(String... message) {

            mChatClient = new ChatClient(new ChatClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            }); // create ChatClient
            mChatClient.setConnectionListener(chatInterface);
            mChatClient.run(IPAddress, Port);

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values[0].contains("Retrieving Login Data...")) {
                chatInterface.connected();
            }
            else {
                mAdapter.appendData(values[0]);
            }
        }
    }

    /**
     * Clean up when user clicks the back - button
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mChatClient != null) {
            try {
                mChatClient.sendMessage(ChatClient.QUIT_MESSAGE);
                mChatClient.stopClient();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void connected() {
        sleep(1000);
        sendStartMsg();
    }

    @UiThread
    void sendStartMsg() {
        // start msg consists of user token and node id
        String startMsg = usr_token + "@" + dec_node_id;
        sendMessage(startMsg);
    }

    @Override
    public void linkClicked(long id, NodeInterface nodeInterface) {
        if (editText != null) {
            editText.append(" @" + id);
        }

        finish();

        String url = RestHelper.GetBaseURLChat();
        String ip = url.substring(0, url.indexOf(':'));
        int port = Integer.valueOf(url.substring(url.indexOf(':') + 1, url.length()));

        new ChatActivity_.IntentBuilder_(this)
                .IPAddress(ip)
                .Port(port)
                .DecisionName(nodeInterface.getName())
                .dec_node_id(id)
                .usr_token(SaveSharedPreference.getUserToken(this))
                .start();
    }
}
