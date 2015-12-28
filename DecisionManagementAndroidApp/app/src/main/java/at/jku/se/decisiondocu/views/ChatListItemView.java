package at.jku.se.decisiondocu.views;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import at.jku.se.decisiondocu.R;
import at.jku.se.decisiondocu.login.SaveSharedPreference;
import at.jku.se.decisiondocu.restclient.client.model.MsgWrapper;

/**
 * Created by martin on 30.11.15.
 */
@EViewGroup(R.layout.viewgroup_chat)
public class ChatListItemView extends LinearLayout {

    @ViewById(R.id.chat_message_layout)
    LinearLayout layout;

    @ViewById(R.id.chat_message_text)
    TextView tv_headline;

    @ViewById(R.id.chat_message_author)
    TextView tv_author;

    @ViewById(R.id.chat_message_timestamp)
    TextView tv_timestamp;

    public ChatListItemView(Context context) {
        super(context);
    }

    public ChatListItemView(Context context, BaseAdapter adapter) {
        this(context);
    }

    public void bind(MsgWrapper item) {

        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;

        if (item.getCreatorEmail() != null && item.getCreatorEmail().equals(SaveSharedPreference.getUserEmail(getContext()))) {
            layout.setBackgroundResource(R.drawable.speech_bubble_green);
            lp.gravity = Gravity.RIGHT;

        } else {
            layout.setBackgroundResource(R.drawable.speech_bubble_orange);
            lp.gravity = Gravity.LEFT;
        }

        layout.setLayoutParams(lp);
        tv_headline.setText(item.getMessage());
        tv_author.setText(item.getCreator());
        tv_timestamp.setText(item.getTimestamp().yyyyMMdd());
    }
}
