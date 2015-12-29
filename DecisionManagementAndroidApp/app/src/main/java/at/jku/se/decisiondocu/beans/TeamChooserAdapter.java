package at.jku.se.decisiondocu.beans;

import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.EBean;

import java.util.List;

import at.jku.se.decisiondocu.restclient.client.model.Project;
import at.jku.se.decisiondocu.views.TeamItemView;
import at.jku.se.decisiondocu.views.TeamItemView_;

/**
 * Created by martin on 28.12.15.
 */
@EBean
public class TeamChooserAdapter extends TeamAdapter {

    @Override
    public void find() {
        showDialog();
        List<Project> projects = mTeamFinder.findAll();
        updateItems(projects);
        dismissDialog();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TeamItemView view;

        if (convertView == null) {
            view = TeamItemView_.build(context, this);
        } else {
            view = (TeamItemView) convertView;
        }

        Project p = getItem(position);
        view.bind(p, true);
        return view;
    }
}
