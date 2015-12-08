package at.jku.se.decisiondocu.fragments;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import at.jku.se.decisiondocu.MainActivity;
import at.jku.se.decisiondocu.R;
import at.jku.se.decisiondocu.beans.Team;
import at.jku.se.decisiondocu.beans.TeamAdapter;
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

    @Bean
    TeamAdapter mAdapter;

    @AfterViews
    void init() {
        mListView.setAdapter(mAdapter);
    }

    @ItemClick(R.id.list_view_teams)
    protected void itemClicked(Project item) {
        Log.i("ListView", "Item " + item.toString() + " clicked!");
        MainActivity.Instance.mViewPager.setCurrentItem(1);
    }
}