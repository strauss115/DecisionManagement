package at.jku.se.decisiondocu.beans;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import at.jku.se.decisiondocu.asynctask.OnAsyncTaskFinished;
import at.jku.se.decisiondocu.asynctask.TeamIconDownloader;
import at.jku.se.decisiondocu.restclient.client.model.Project;
import at.jku.se.decisiondocu.views.TeamItemView;
import at.jku.se.decisiondocu.views.TeamItemView_;

/**
 * Created by martin on 24.11.15.
 */
@EBean
public class TeamAdapter extends BaseAdapter implements OnAsyncTaskFinished {

    private List<Project> mItems;

    @Bean(RESTProjectFinder.class)
    TeamFinder mTeamFinder;

    @AfterInject
    void initAdapter() {
        mItems = mTeamFinder.findAll();
        for (Project t : mItems) {
            //new TeamIconDownloader(t, this).execute();
        }
    }

    @RootContext
    Context context;

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Project getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TeamItemView view;

        if (convertView == null) {
            view = TeamItemView_.build(context, this);
        } else {
            view = (TeamItemView) convertView;
        }

        view.bind(getItem(position));
        return view;
    }

    @Override
    public void finished() {
        notifyDataSetChanged();
    }
}
