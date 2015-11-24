package at.jku.se.decisiondocu.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import at.jku.se.decisiondocu.beans.SearchAdapter;
import at.jku.se.decisiondocu.beans.Team;
import at.jku.se.decisiondocu.beans.TeamAdapter;

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
    protected void itemClicked(Team item) {
        Log.i("ListView", "Item " + item.toString() + " clicked!");
    }
}