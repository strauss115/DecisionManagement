package at.jku.se.decisiondocu.beans.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import at.jku.se.decisiondocu.restclient.client.DBStrings.RelationString;
import at.jku.se.decisiondocu.restclient.client.model.NodeInterface;
import at.jku.se.decisiondocu.restclient.client.model.RelationshipInterface;
import at.jku.se.decisiondocu.views.SearchDetailListItemView;
import at.jku.se.decisiondocu.views.SearchDetailListItemView_;

/**
 * Created by martin on 24.11.15.
 *
 * Adapter, used for the Decision Details Activity.
 * It contains a list of String - NodeInterface Pairs and uses SearchDetailListItemView.
 *
 */
@EBean
public class RelationAdapter extends BaseAdapter {

    private List<Pair<String, NodeInterface>> items;
    private NodeInterface node;

    @RootContext
    Context context;

    @AfterInject
    void initAdapter() {
        items = new ArrayList<>();
    }

    void prepareRelation() {
        try {
            items = new ArrayList<>();
            Map<String, List<RelationshipInterface>> rels = node.getRelationships();
            for (String key : rels.keySet()) {
                if(key!= RelationString.HAS_MESSAGE){
                    for (RelationshipInterface rel : rels.get(key)) {
                        items.add(new Pair<String, NodeInterface>(key, rel.getRelatedNode()));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Update items
     * @param node
     */
    @UiThread
    public void updateItems(NodeInterface node) {
        if (node != null) {
            this.node = node;
        }
        prepareRelation();
        notifyDataSetChanged();
    }

    /**
     * Returns number of items
     * @return
     */
    @Override
    public int getCount() {
        try {
            return items.size();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<String, NodeInterface> getItem(int position) {
        return items.get(position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchDetailListItemView view;

        if (convertView == null) {
            view = SearchDetailListItemView_.build(context, this);
        } else {
            view = (SearchDetailListItemView) convertView;
        }
        Pair<String, NodeInterface> pair = getItem(position);
        view.bind(pair.second, pair.first);
        return view;
    }
}
