package at.jku.se.decisiondocu.views;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import at.jku.se.decisiondocu.R;
import at.jku.se.decisiondocu.restclient.client.model.NodeInterface;

/**
 * Created by martin on 23.11.15.
 */
@EViewGroup(R.layout.viewgroup_searchlistitem)
public class SearchDetailListItemView extends LinearLayout {

    @ViewById(R.id.node_name)
    TextView node_name;

    @ViewById(R.id.relation_name)
    TextView relation_name;

    @ViewById(R.id.relation_creation_date)
    TextView creation_date;

    @ViewById(R.id.relation_type)
    TextView relation_type;

    private BaseAdapter mAdapter;
    private NodeInterface node;
    private String relation;

    public SearchDetailListItemView(Context context) {
        super(context);
    }

    public SearchDetailListItemView(Context context, BaseAdapter adapter) {
        this(context);
        this.mAdapter=adapter;
    }

    public void bind(NodeInterface item, String rel) {
        try {
            node = item;
            relation = rel;

            relation_name.setText(relation);
            relation_type.setText("Typ: "+node.getClass().getSimpleName());
            node_name.setText(node.getName());
            if(node.getCreationDate()!=null) {
                creation_date.setText("Date: " + node.getCreationDate().yyyyMMdd());
            }
        }catch (Exception e){
            e.printStackTrace();
            //Log.d("Dec",item.toString());
        }
    }
}
