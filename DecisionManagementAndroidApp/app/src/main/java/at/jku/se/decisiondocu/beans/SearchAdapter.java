package at.jku.se.decisiondocu.beans;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import at.jku.se.decisiondocu.views.ListItemView;
import at.jku.se.decisiondocu.views.ListItemView_;

/**
 * Created by martin on 23.11.15.
 */
@EBean
public class SearchAdapter extends BaseAdapter implements Filterable {

    private Filter mSearchFilter;
    private List<String> mItems;
    private List<String> mItemsOrig;
    private Comparator<String> mComparator;

    @RootContext
    Context context;

    @AfterInject
    void initAdapter() {
        mComparator = sComparatorName;
        mItems = mItemsOrig = new ArrayList<>();
        mSearchFilter = new SearchFilter();
        mItems.add("Decision xyz");
        mItems.add("Decision 334");
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
    public Object getItem(int position) {
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

        view.bind((String)getItem(position));
        return view;
    }

    @Override
    public Filter getFilter() {
        return mSearchFilter;
    }


    public static Comparator<String> sComparatorName = new Comparator<String>() {
        @Override
        public int compare(String lhs, String rhs) {
            return lhs.compareTo(rhs);
        }
    };

    public static Comparator<String> sComparatorReverseName = new Comparator<String>() {
        @Override
        public int compare(String lhs, String rhs) {
            return rhs.compareTo(lhs);
        }
    };

    public void refresh() {
        getData();
        notifyDataSetChanged();
    }

    public void setComparator(Comparator<String> c) {
        this.mComparator = c;
        refresh();
    }

    private class SearchFilter extends Filter {


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                List<String> filterList = new ArrayList<>();
                String constraintStr = constraint.toString().toLowerCase();
                for (int i = 0; i < mItems.size(); i++) {
                    String headline = mItems.get(i);
                    String desc = mItems.get(i);

                    if (headline != null && headline.toLowerCase().contains(constraintStr)) {
                        filterList.add(mItems.get(i));
                    }
                    else if (desc != null && desc.toLowerCase().contains(constraintStr)) {
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
                mItems = (List<String>) results.values;
                notifyDataSetChanged();
            }
        }
    }

}
