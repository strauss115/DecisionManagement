package at.jku.se.decisiondocu.chat;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import at.jku.se.decisiondocu.R;
import at.jku.se.decisiondocu.beans.ChatAdapter;
import at.jku.se.decisiondocu.login.SaveSharedPreference;
import at.jku.se.decisiondocu.restclient.RestClient;
import at.jku.se.decisiondocu.restclient.client.ApiException;
import at.jku.se.decisiondocu.restclient.client.api.RelationshipApi;
import at.jku.se.decisiondocu.restclient.client.model.NodeInterface;
import at.jku.se.decisiondocu.restclient.client.model.User;

import static android.os.SystemClock.sleep;

@EActivity(R.layout.activity_chat)
public class ChatActivity extends AppCompatActivity implements ChatInterface {

    protected ProgressDialog mDialog;

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

    @ViewById(R.id.send_button)
    Button send;

    @Bean
    ChatAdapter mAdapter;

    private Client mClient;

    private Map<String, String> mMap;

    @Click(R.id.send_button)
    void OnBtnClick() {
        String message = editText.getText().toString();
        if (isMsgValid(message)) {
            sendMessage(message);
            editText.setText("");
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
            this.mMap = map;

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
        new connectTask().execute("");

        // start msg consists of user token and node id
        String startMsg = usr_token + "@" + dec_node_id;

        // AWUR
        sleep(250);
        sendMessage(startMsg);
        // AWUR ende
    }

    @UiThread
    void showDialog() {
        Log.d("dialog", "showing");
        if (mDialog == null) {
            mDialog = new ProgressDialog(this);
            mDialog.setMessage("please wait...");
            mDialog.show();
        }
    }

    @UiThread
    void dismissDialog() {
        Log.d("dialog", "hiding");
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

    private void sendMessage(String message) {
        if (mClient != null) {
            mClient.sendMessage(message);
        }
    }

    public class connectTask extends AsyncTask<String, String, Client> {

        @Override
        protected Client doInBackground(String... message) {

            //we create a Client object and
            mClient = new Client(new Client.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
            mClient.run(IPAddress, Port);

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            mAdapter.appendData(values[0]);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mClient != null) {
            try {
                mClient.sendMessage(Client.QUIT_MESSAGE);
                mClient.stopClient();
            } catch (Exception e) {
                Log.d("tag", e.getMessage());
            }
        }
    }

    @Override
    public void linkClicked(long id, NodeInterface nodeInterface) {
        if (editText != null) {
            editText.append(" @" + id);
        }
    }
}
