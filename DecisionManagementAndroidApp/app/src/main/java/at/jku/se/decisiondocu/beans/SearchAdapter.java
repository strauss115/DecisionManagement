package at.jku.se.decisiondocu.beans;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import at.jku.se.decisiondocu.restclient.client.model.Decision;
import at.jku.se.decisiondocu.restclient.client.model.Project;
import at.jku.se.decisiondocu.views.ListItemView;
import at.jku.se.decisiondocu.views.ListItemView_;

/**
 * Created by martin on 23.11.15.
 */
@EBean
public class SearchAdapter extends BaseAdapter implements Filterable {

    private Filter mSearchFilter;
    private List<Decision> mItems;
    private List<Decision> mItemsOrig;
    private Comparator<Decision> mComparator;
    private ProgressDialog mDialog;

    @Bean(RESTDecisionFinder.class)
    DecisionFinder mDecisionFinder;

    @RootContext
    Context context;

    @AfterInject
    void initAdapter() {
        mComparator = sComparatorName;
        mItems = mItemsOrig = new ArrayList<>();
        mSearchFilter = new SearchFilter();
        findAll();
    }

    @Background
    void findAll() {
        showDialog();
        List<Decision> decisions = mDecisionFinder.findAll();
        updateItems(decisions);
        dismissDialog();
    }

    @UiThread
    void showDialog() {
        Log.d("dialog", "showing");
        if (mDialog == null) {
            mDialog = new ProgressDialog(context);
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
    void updateItems(List<Decision> items) {
        mItems = items;
        notifyDataSetChanged();
        getData();
    }

    private void getData() {
        Collections.sort(mItems, mComparator);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Decision getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemView view;

        if (convertView == null) {
            view = ListItemView_.build(context, this);
        } else {
            view = (ListItemView) convertView;
        }

        view.bind(getItem(position));
        return view;
    }

    @Override
    public Filter getFilter() {
        return mSearchFilter;
    }


    public static Comparator<Decision> sComparatorName = new Comparator<Decision>() {
        @Override
        public int compare(Decision lhs, Decision rhs) {
            return lhs.getName().compareTo(rhs.getName());
        }
    };

    public static Comparator<Decision> sComparatorReverseName = new Comparator<Decision>() {
        @Override
        public int compare(Decision lhs, Decision rhs) {
            return rhs.getName().compareTo(lhs.getName());
        }
    };

    public void refresh() {
        getData();
        notifyDataSetChanged();
    }

    public void setComparator(Comparator<Decision> c) {
        this.mComparator = c;
        refresh();
    }

    private class SearchFilter extends Filter {


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                List<Decision> filterList = new ArrayList<>();
                String constraintStr = constraint.toString().toLowerCase();
                for (int i = 0; i < mItems.size(); i++) {
                    String headline = mItems.get(i).getName();

                    if (headline != null && headline.toLowerCase().contains(constraintStr)) {
                        filterList.add(mItems.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                synchronized (this) {
                    results.count = mItemsOrig.size();
                    results.values = mItemsOrig;
                }
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                mItems = (List<Decision>) results.values;
                notifyDataSetChanged();
            }
        }
    }

}
