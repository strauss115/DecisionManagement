package at.jku.se.decisiondocu.views;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import at.jku.se.decisiondocu.R;

/**
 * Created by martin on 30.11.15.
 */
@EViewGroup(R.layout.viewgroup_chat)
public class ChatListItemView extends LinearLayout {

    @ViewById(R.id.chat_message_text)
    TextView tv_headline;


    public ChatListItemView(Context context) {
        super(context);
    }

    public ChatListItemView(Context context, BaseAdapter adapter) {
        this(context);
    }

    public void bind(String item) {
        if (item.contains("Hans")) {
            tv_headline.setBackgroundResource(R.drawable.speech_bubble_orange);
        } else {
            tv_headline.setBackgroundResource(R.drawable.speech_bubble_green);
        }
        tv_headline.setText(item);
    }
}
