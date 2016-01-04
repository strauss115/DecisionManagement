package at.jku.se.decisiondocu.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.HashMap;

import at.jku.se.decisiondocu.R;
import at.jku.se.decisiondocu.asynctask.RestNetworkTasks;
import at.jku.se.decisiondocu.beans.interfaces.NodeFinder;
import at.jku.se.decisiondocu.beans.RESTNodeFinder;
import at.jku.se.decisiondocu.beans.adapters.RelationAdapter;
import at.jku.se.decisiondocu.restclient.client.DBStrings.RelationString;
import at.jku.se.decisiondocu.restclient.client.model.Document;
import at.jku.se.decisiondocu.restclient.client.model.NodeInterface;


/**
 * Created by martin on 23.12.15.
 */
@EActivity(R.layout.activity_search_document_details)
public class SearchDocumentDetailsActivity extends AppCompatActivity {

    private ProgressDialog mDialog;
    private NodeInterface mNode;
    private HashMap<LinearLayout,Integer> mLinearLayoutHashMap;

    @ViewById(R.id.search_document_detail_title)
    TextView mTitle;

    @ViewById(R.id.document_id)
    TextView mId;

    @ViewById(R.id.document_creation_date)
    TextView mCreationDate;

    @ViewById(R.id.search_document_detail_nodetype)
    TextView mNodeType;

    @ViewById(R.id.document_image)
    ImageView imageView;

    @ViewById(R.id.search_detail_document_list_view)
    ListView mRelationships;

    @Extra
    long decisionId = -1;

    @Extra
    String relation = "";

    File documentFile;
    boolean error = true;

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

    @Click(R.id.document_image)
    protected void clickedImage(){
        try {
            if(!error) {
                if (relation.equals(RelationString.HAS_PICTURE) && imageView.getDrawable() != null) {
                    ImageView image = new ImageView(this);
                    image.setImageDrawable(imageView.getDrawable());

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(this).
                                    setMessage(mNode.getName()).
                                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setView(image);
                    builder.create().show();
                }else if (relation.equals(RelationString.HAS_DOCUMENT)&&documentFile!=null&&documentFile.exists()){
                    Uri path = Uri.fromFile(documentFile);
                    Intent pdfIntent = new Intent(Intent.ACTION_VIEW);

                    String[] strings =documentFile.getName().split("\\.");
                    String fileextension="";
                    if(strings.length>1) {
                        fileextension = strings[strings.length - 1];
                        pdfIntent.setDataAndType(path, "application/" + fileextension);
                    }
                    Log.d("File","Opening File: "+documentFile.getName()+" extension: "+fileextension);
                    pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    try {
                        startActivity(pdfIntent);
                    }catch (Exception e){
                        Toast.makeText(this,
                                "No Application Available to View PDF",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @ItemClick(R.id.search_detail_list_view)
    protected void itemClicked(Pair<String, NodeInterface> item) {
        //Log.i("ListView", "Item " + item + " clicked!");
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

    @UiThread
    void updateView() {
        try {
            mAdapter.updateItems(mNode);
            mTitle.setText(mNode.getName());
            mId.setText("ID: " + mNode.getId() + "");
            if (mNode.getCreationDate() != null) {
                mCreationDate.setText("Date: " + mNode.getCreationDate().yyyyMMdd());
            }
            mNodeType.setText(mNode.getClass().getSimpleName());

            mRelationships.setAdapter(mAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(relation.equals(RelationString.HAS_PICTURE)) {
            try {
                new RestNetworkTasks.DownloadProfilPicture(null, null, null, decisionId, imageView) {
                    @Override
                    protected void onPostExecute(Integer success) {
                        super.onPostExecute(success);
                        if (success > 0 && this.image != null) {
                            try {
                                imageView.setImageBitmap(this.image);
                                error = false;
                            } catch (Exception e) {
                                e.printStackTrace();
                                error = true;
                            }
                        }else{
                            error = true;
                        }
                    }
                }.execute();
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            }
        }
        else if(relation.equals(RelationString.HAS_DOCUMENT)){
            try {
                new RestNetworkTasks.DownloadDocument(null, null, null, (Document)mNode) {
                    @Override
                    protected void onPostExecute(Integer success) {
                        super.onPostExecute(success);
                        if (success > 0) {
                            try {
                                documentFile = this.file;
                                imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.document, null));
                                error = false;
                            } catch (Exception e) {
                                e.printStackTrace();
                                error = true;
                            }
                        }else{
                            error = true;
                        }
                    }
                }.execute();
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            }
        }
    }




}
