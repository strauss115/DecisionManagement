package at.jku.se.decisiondocu.views;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import at.jku.se.decisiondocu.R;
import at.jku.se.decisiondocu.chat.ChatActivity;
import at.jku.se.decisiondocu.chat.ChatActivity_;
import at.jku.se.decisiondocu.fragments.ChatFragment;
import at.jku.se.decisiondocu.restclient.client.model.Decision;

import static at.jku.se.decisiondocu.R.id.decision_startChat;

/**
 * Created by martin on 23.11.15.
 */
@EViewGroup(R.layout.viewgroup_listitem)
public class ListItemView extends LinearLayout {

    @ViewById(R.id.decision_name)
    TextView tv_headline;

    @ViewById(R.id.decision_author)
    TextView tv_author;

    @ViewById(R.id.decision_creation_date)
    TextView tv_date;

    @Click(R.id.decision_startChat)
    void click() {
        new ChatActivity_.IntentBuilder_(getContext())
                .IPAddress("192.168.0.101")
                .UserName("5861")
                .DecisionName("5898")
                .start();
    }

    public ListItemView(Context context) {
        super(context);
    }

    public ListItemView(Context context, BaseAdapter adapter) {
        this(context);
    }

    public void bind(Decision item) {
        tv_author.setText("Author: " + item.getId());
        tv_date.setText("Date: " + item.getCreationDate().toString());
        tv_headline.setText(item.getName());
    }
}
