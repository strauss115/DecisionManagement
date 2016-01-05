package at.jku.se.decisiondocu.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

import at.jku.se.decisiondocu.R;
import at.jku.se.decisiondocu.beans.RESTNodeFinder;
import at.jku.se.decisiondocu.beans.adapters.RelationAdapter;
import at.jku.se.decisiondocu.beans.interfaces.NodeFinder;
import at.jku.se.decisiondocu.login.SaveSharedPreference;
import at.jku.se.decisiondocu.restclient.RestHelper;
import at.jku.se.decisiondocu.restclient.client.DBStrings.PropertyString;
import at.jku.se.decisiondocu.restclient.client.model.Document;
import at.jku.se.decisiondocu.restclient.client.model.NodeInterface;


/**
 * Created by martin on 23.12.15.
 */
@EActivity(R.layout.activity_search_node_details)
public class SearchNodeDetailsActivity extends AppCompatActivity {

    private ProgressDialog mDialog;
    private NodeInterface mNode;
    private HashMap<LinearLayout,Integer> mLinearLayoutHashMap;

    @ViewById(R.id.search_detail_title)
    TextView mTitle;

    @ViewById(R.id.node_id)
    TextView mId;

    @ViewById(R.id.node_creation_date)
    TextView mCreationDate;

    @ViewById(R.id.search_detail_nodetype)
    TextView mNodeType;

    @ViewById(R.id.search_detail_dp_placeholder)
    LinearLayout mDirectProperties;

    @ViewById(R.id.search_detail_list_view)
    ListView mRelationships;

    @Extra
    long decisionId = -1;

    @Bean(RESTNodeFinder.class)
    NodeFinder mNodeFinder;

    @Bean
    RelationAdapter mAdapter;

    public NodeInterface getmNode() {
        return mNode;
    }

    @AfterInject
    void init() {
        mLinearLayoutHashMap = new HashMap<>();
        if (decisionId >= 0) {
            fetchData();
        }
    }

    @Background
    void fetchData() {
        showDialog();
        mNode = mNodeFinder.find(decisionId);
        if (mNode != null) updateView();
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

    @ItemClick(R.id.search_detail_list_view)
    protected void itemClicked(Pair<String, NodeInterface> item) {
        if(item.second instanceof Document) {
            new SearchDocumentDetailsActivity_.IntentBuilder_(this)
                    .relation(item.first)
                    .decisionId(item.second.getId())
                    .start();
        }else {
            new SearchNodeDetailsActivity_.IntentBuilder_(this)
                    .decisionId(item.second.getId())
                    .start();
        }
        finish();
    }

    @Click(R.id.node_header)
    public void openChat(){
        try {
            String url = RestHelper.GetBaseURLChat();
            String ip = url.substring(0, url.indexOf(':'));
            int port = Integer.valueOf(url.substring(url.indexOf(':') + 1, url.length()));

            new ChatActivity_.IntentBuilder_(this)
                    .IPAddress(ip)
                    .Port(port)
                    .DecisionName(mNode.getName())
                    .dec_node_id(mNode.getId())
                    .usr_token(SaveSharedPreference.getUserToken(this))
                    .start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @UiThread
    void updateView() {
        mAdapter.updateItems(mNode);
        mTitle.setText(mNode.getName());
        mId.setText("ID: "+mNode.getId() + "");
        if(mNode.getCreationDate()!=null) {
            mCreationDate.setText("Date: "+mNode.getCreationDate().yyyyMMdd());
        }
        mNodeType.setText(mNode.getClass().getSimpleName());

        if (mNode.getDirectProperties() != null) {
            for (Map.Entry<String, String> entry : mNode.getDirectProperties().entrySet()) {
                if (entry.getKey().toLowerCase().equals(PropertyString.CREATIONDATE.toLowerCase())) continue;
                printTextView(entry.getKey() + " --> " + entry.getValue(), mDirectProperties);
            }
        }

        if (!mLinearLayoutHashMap.containsKey(mDirectProperties)) {
            printTextView("-", mDirectProperties);
        }

        mRelationships.setAdapter(mAdapter);



        /*if (mNode.getRelationships() != null) {
            for (Map.Entry<String, List<RelationshipInterface>> entry : mNode.getRelationships().entrySet()) {

                for (RelationshipInterface relationshipInterface : entry.getValue()) {

                    NodeInterface node = relationshipInterface.getRelatedNode();
                    Log.d("node", node.getNodeType());

                    switch (node.getNodeType()) {
                        case NodeString.PROPERTY:
                            printTextView("Property: " + node.getName(), mRelationships);
                            break;
                        case NodeString.ALTERNATIVE:
                            Alternative a = (Alternative)node;
                            printTextView("Alternative: " + a.getName(), mRelationships);
                        default:
                            break;
                    }


                /*TextView tv = new TextView(this);
                tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                tv.setText(entry.getKey() + " --> " + node.getName() + "(" + node.getNodeType() + ")");
                testView.addView(tv);*/

           /*     }

            }
        }*/
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
        tv.setTextColor(Color.BLACK);

        int value = 1;
        if (mLinearLayoutHashMap.containsKey(view)) {
            value = mLinearLayoutHashMap.get(view);
        }
        mLinearLayoutHashMap.put(view, value);
        view.addView(tv);
    }
}
