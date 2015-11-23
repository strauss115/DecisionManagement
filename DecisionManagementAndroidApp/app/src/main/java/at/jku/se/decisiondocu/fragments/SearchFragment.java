package at.jku.se.decisiondocu.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.ListFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import at.jku.se.decisiondocu.R;
import at.jku.se.decisiondocu.beans.SearchAdapter;
import at.jku.se.decisiondocu.fragments.dummy.DummyContent;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
@EFragment(R.layout.fragment_search)
public class SearchFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    @ViewById(R.id.list_view)
    ListView mListView;

    @ViewById(R.id.spinner1)
    Spinner mSpinner;

    @ViewById(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bean
    SearchAdapter mAdapter;

    @AfterViews
    void init() {
        mListView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
        mSpinner.setOnItemSelectedListener(this);
        mSpinner.setSelection(0);

    }

    @TextChange(R.id.inputSearch)
    protected void onTextChangesOnInputSearch(CharSequence text, int before, int start, int count) {
        mAdapter.getFilter().filter(text);
    }

    @ItemClick(R.id.list_view)
    protected void itemClicked(String item) {
        Log.i("ListView", "Item " + item + " clicked!");
    }

    private void refreshContent() {
        mAdapter.refresh();
        mSwipeRefreshLayout.setRefreshing(false);
        mListView.smoothScrollToPosition(0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        setComparatorForAdapter(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    private void setComparatorForAdapter(int id) {
        Log.i("Comparator", "changed!");
        switch(id) {
            case 0:
                mAdapter.setComparator(SearchAdapter.sComparatorName);
                break;
            case 1:
                mAdapter.setComparator(SearchAdapter.sComparatorName);
                break;
        }
    }
}
