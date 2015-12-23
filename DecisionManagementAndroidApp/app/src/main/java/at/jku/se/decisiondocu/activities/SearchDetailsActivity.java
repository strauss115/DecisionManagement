package at.jku.se.decisiondocu.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import at.jku.se.decisiondocu.R;
import at.jku.se.decisiondocu.beans.DecisionFinder;
import at.jku.se.decisiondocu.beans.RESTDecisionFinder;
import at.jku.se.decisiondocu.restclient.client.model.Decision;
import at.jku.se.decisiondocu.restclient.client.model.NodeInterface;
import at.jku.se.decisiondocu.restclient.client.model.NodeString;
import at.jku.se.decisiondocu.restclient.client.model.RelationString;
import at.jku.se.decisiondocu.restclient.client.model.RelationshipInterface;
import at.jku.se.decisiondocu.restclient.client.model.User;

/**
 * Created by martin on 23.12.15.
 */
@EActivity(R.layout.activity_search_details)
public class SearchDetailsActivity extends AppCompatActivity {

    private ProgressDialog mDialog;
    private Decision mDecision;


    @ViewById(R.id.search_detail_title)
    TextView mTitle;

    @ViewById(R.id.search_detail_id)
    TextView mId;

    @ViewById(R.id.search_detail_author)
    TextView mAuthor;

    @ViewById(R.id.search_detail_creationdate)
    TextView mCreationDate;

    @ViewById(R.id.search_detail_dp_placeholder)
    LinearLayout mDirectProperties;

    @ViewById(R.id.search_detail_r_placeholder)
    LinearLayout mRelationships;

    @Extra
    long decisionId = -1;

    @Bean(RESTDecisionFinder.class)
    DecisionFinder mDecisionFinder;

    @AfterInject
    void init() {
        if (decisionId >= 0) {
            fetchData();
        }
    }

    @Background
    void fetchData() {
        showDialog();
        mDecision = mDecisionFinder.find(decisionId);
        if (mDecision != null) updateView();
        dismissDialog();
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

    @UiThread
    void updateView() {
        mTitle.setText(mDecision.getName());
        mId.setText(mDecision.getId() + "");
        mAuthor.setText("unknown");
        List<RelationshipInterface> creators = mDecision.getRelationships().get(RelationString.CREATOR);
        if (creators != null && creators.size() > 0) {
            try {
                User u = (User) creators.get(0).getRelatedNode();
                mAuthor.setText(u.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mCreationDate.setText(mDecision.getCreationDate().yyyyMMdd());

        for (Map.Entry<String, String> entry : mDecision.getDirectProperties().entrySet()) {
            printTextView(entry.getKey() + " --> " + entry.getValue(), mDirectProperties);
        }

        for (Map.Entry<String, List<RelationshipInterface>> entry : mDecision.getRelationships().entrySet()) {

            for (RelationshipInterface relationshipInterface : entry.getValue()) {

                NodeInterface node = relationshipInterface.getRelatedNode();
                Log.d("node", node.getNodeType());

                switch (node.getNodeType()) {
                    case NodeString.USER:
                        printTextView("User: " + ((User)node).getName() + " " + ((User)node).getLastname(), mRelationships);
                        break;
                    case NodeString.PROPERTY:
                        printTextView("Property: " + node.getName(), mRelationships);
                        break;
                    default:
                        break;
                }


                /*TextView tv = new TextView(this);
                tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                tv.setText(entry.getKey() + " --> " + node.getName() + "(" + node.getNodeType() + ")");
                testView.addView(tv);*/

            }

        }


    }

    private void printTextView(String content, LinearLayout view) {
        TextView tv = new TextView(this);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        if (Build.VERSION.SDK_INT < 23) {
            tv.setTextAppearance(this, android.R.style.TextAppearance_Medium);
        } else {
            tv.setTextAppearance(android.R.style.TextAppearance_Medium);
        }
        tv.setText(content);
        view.addView(tv);
    }
}
