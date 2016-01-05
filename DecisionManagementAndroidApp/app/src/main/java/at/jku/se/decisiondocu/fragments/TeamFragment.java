package at.jku.se.decisiondocu.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import at.jku.se.decisiondocu.MainActivity;
import at.jku.se.decisiondocu.R;
import at.jku.se.decisiondocu.beans.adapters.TeamAdapter;
import at.jku.se.decisiondocu.restclient.client.model.Project;

@EFragment(R.layout.fragment_team)
public class TeamFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    private static final String ARG_SECTION_NUMBER = "section_number";

    @ViewById(R.id.list_view_teams)
    ListView mListView;

    @ViewById(R.id.swipe_container_team)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bean
    TeamAdapter mAdapter;

    @AfterViews
    void init() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

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

    }

    private void refreshContent() {
        mAdapter.refresh();
        mSwipeRefreshLayout.setRefreshing(false);
        mListView.smoothScrollToPosition(0);
    }

    @ItemClick(R.id.list_view_teams)
    protected void itemClicked(Project item) {
        Log.i("ListView", "Item " + item.toString() + " clicked!");
        MainActivity.Instance.mViewPager.setCurrentItem(1);
    }
}