package at.jku.se.decisiondocu.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import at.jku.se.decisiondocu.R;
import at.jku.se.decisiondocu.activities.SearchNodeDetailsActivity_;
import at.jku.se.decisiondocu.beans.adapters.SearchAdapter;
import at.jku.se.decisiondocu.restclient.client.model.Decision;

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

        TextView empty = new TextView(getActivity());
        empty.setHeight(250);
        mListView.addFooterView(empty);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (mListView == null || mListView.getChildCount() == 0) ?
                                0 : mListView.getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });


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
    protected void itemClicked(Decision item) {
        //Log.i("ListView", "Item " + item + " clicked!");
        new SearchNodeDetailsActivity_.IntentBuilder_(getActivity())
                .decisionId(item.getId())
                .start();
    }

    private void refreshContent() {
        mAdapter.refresh();
        mSwipeRefreshLayout.setRefreshing(false);
        mListView.smoothScrollToPosition(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        setComparatorForAdapter(position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    private void setComparatorForAdapter(int id) {
        Log.i("Comparator", "changed!");
        switch(id) {
            case 0:
                mAdapter.setComparator(SearchAdapter.sComparatorName);
                break;
            case 1:
                mAdapter.setComparator(SearchAdapter.sComparatorDate);
                break;
            case 2:
                mAdapter.setComparator(SearchAdapter.sComparatorAuthor);
                break;
            case 3:
                mAdapter.setComparator(SearchAdapter.sComparatorFavourite);
                break;
            default:
                mAdapter.setComparator(SearchAdapter.sComparatorName);
                break;
        }
    }
}
