package at.jku.se.decisiondocu.chat;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import at.jku.se.decisiondocu.R;
import at.jku.se.decisiondocu.beans.ChatAdapter;
import at.jku.se.decisiondocu.login.SaveSharedPreference;

import static android.os.SystemClock.sleep;

@EActivity(R.layout.activity_chat)
public class ChatActivity extends AppCompatActivity {

    private static final int PORT = 2222;

    @Extra
    long dec_node_id;

    @Extra
    String usr_token;

    @Extra
    String DecisionName;

    @Extra
    String IPAddress;

    @ViewById(R.id.list)
    ListView mList;

    @ViewById(R.id.editText)
    EditText editText;

    @ViewById(R.id.send_button)
    Button send;

    @ViewById(R.id.chatheader)
    TextView chat_header;

    @Bean
    ChatAdapter mAdapter;

    private Client mClient;

    @Click(R.id.send_button)
    void OnBtnClick() {
        String message = editText.getText().toString();
        if (isMsgValid(message)) {
            sendMessage(message);
            editText.setText("");
        }
    }

    @AfterViews
    void init() {
        chat_header.setText(usr_token+"@"+DecisionName);
        mList.setAdapter(mAdapter);

        // connect to the server
        new connectTask().execute("");

        // AWUR
        sleep(250);
        sendMessage(usr_token + "@" + dec_node_id); // Username
        // AWUR ende
    }

    private boolean isMsgValid(String msg) {
        return msg.length() > 0;
    }

    private void sendMessage(String message) {
        if (mClient != null) {
            mClient.sendMessage(message);
        }
    }

    public class connectTask extends AsyncTask<String,String,Client> {

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
            mClient.run(IPAddress, PORT);

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
}
