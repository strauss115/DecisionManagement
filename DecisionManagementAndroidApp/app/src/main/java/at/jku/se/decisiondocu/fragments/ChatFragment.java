package at.jku.se.decisiondocu.fragments;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.EditText;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import at.jku.se.decisiondocu.R;
import at.jku.se.decisiondocu.chat.ChatActivity_;

/**
 * Created by martin on 30.11.15.
 */
@EFragment(R.layout.fragment_chat)
public class ChatFragment extends Fragment {

    public static final String IP_ADDRESS = "192.168.0.101";
    public static final String USERNAME = "5861";
    public static final String DECISION_NAME = "5884";

    @ViewById(R.id.chat_ip_address)
    EditText mIPAddress;

    @ViewById(R.id.chat_user_name)
    EditText mUserName;

    @ViewById(R.id.chat_decision_name)
    EditText mDecisionName;

    @AfterViews
    void init() {
        mIPAddress.setText(IP_ADDRESS);
        mUserName.setText(USERNAME);
        mDecisionName.setText(DECISION_NAME);
    }

    @Click(R.id.testbutton)
    void click() {
        Log.d("btn", "clicked");

        boolean error = false;
        String ip = mIPAddress.getText().toString();
        String user = mUserName.getText().toString();
        String decision = mDecisionName.getText().toString();

        if (!isIPValid(ip)) {
            error = true;
            mIPAddress.setError("Invalid IP-Address");
        }

        if (!isUserValid(user)) {
            error = true;
            mUserName.setError("Invalid User Name");
        }

        if (!isDecisionValid(decision)) {
            error = true;
            mIPAddress.setError("Invalid Decision");
        }

        if (!error) {
            new ChatActivity_.IntentBuilder_(getActivity())
                    .IPAddress(ip)
                    .UserName(user)
                    .DecisionName(decision)
                    .start();
        }
    }

    private boolean isIPValid(String ip) {
        //TODO: Replace this with your own logic
        return ip.length() > 0;
    }

    private boolean isUserValid(String user) {
        //TODO: Replace this with your own logic
        return user.length() > 0;
    }

    private boolean isDecisionValid(String decision) {
        //TODO: Replace this with your own logic
        return decision.length() > 0;
    }
}
