package at.jku.se.decisiondocu.views;

import android.content.Context;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import at.jku.se.decisiondocu.R;
import at.jku.se.decisiondocu.activities.ChatActivity_;
import at.jku.se.decisiondocu.login.SaveSharedPreference;
import at.jku.se.decisiondocu.restclient.RestHelper;
import at.jku.se.decisiondocu.restclient.client.model.Decision;
import at.jku.se.decisiondocu.restclient.client.model.RelationString;
import at.jku.se.decisiondocu.restclient.client.model.RelationshipInterface;
import at.jku.se.decisiondocu.restclient.client.model.User;

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

    long dec_node_id;

    @Click(R.id.decision_startChat)
    void click() {
        String url = RestHelper.GetBaseURLChat();
        String ip = url.substring(0, url.indexOf(':'));
        int port = Integer.valueOf(url.substring(url.indexOf(':') + 1, url.length()));

        new ChatActivity_.IntentBuilder_(getContext())
                .IPAddress(ip)
                .Port(port)
                .DecisionName(tv_headline.getText().toString())
                .dec_node_id(dec_node_id)
                .usr_token(SaveSharedPreference.getUserToken(this.getContext()))
                .start();
    }

    public ListItemView(Context context) {
        super(context);
    }

    public ListItemView(Context context, BaseAdapter adapter) {
        this(context);
    }

    public void bind(Decision item) {
        try {

            String mCreator = "unknown";
            List<RelationshipInterface> creators = item.getRelationships().get(RelationString.CREATOR);
            if (creators != null && creators.size() > 0) {
                try {
                    User u = (User)creators.get(0).getRelatedNode();
                    mCreator = u.getEmail();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            tv_author.setText("Author: " + mCreator);
            tv_date.setText("Date: " + item.getCreationDate().yyyyMMdd());
            tv_headline.setText(item.getName());
            dec_node_id = item.getId();
        }catch (Exception e){
            //e.printStackTrace();
            Log.d("Dec",item.toString());
        }
    }
}
