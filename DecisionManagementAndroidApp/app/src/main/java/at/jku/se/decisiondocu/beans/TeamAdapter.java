package at.jku.se.decisiondocu.beans;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.List;

import at.jku.se.decisiondocu.asynctask.OnAsyncTaskFinished;
import at.jku.se.decisiondocu.restclient.client.model.Project;
import at.jku.se.decisiondocu.views.TeamItemView;
import at.jku.se.decisiondocu.views.TeamItemView_;

/**
 * Created by martin on 24.11.15.
 */
@EBean
public class TeamAdapter extends BaseAdapter implements OnAsyncTaskFinished {

    protected ProgressDialog mDialog;
    protected List<Project> mItems;

    @Bean(RESTProjectFinder.class)
    protected TeamFinder mTeamFinder;

    @AfterInject
    void initAdapter() {
        mItems = new ArrayList<>();
        find();
    }

    @Background
    public void find() {
        showDialog();
        List<Project> projects = mTeamFinder.find();
        updateItems(projects);
        dismissDialog();
    }

    @UiThread
    void updateItems(List<Project> items) {
        mItems = items;
        notifyDataSetChanged();
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

    @RootContext
    Context context;

    @Override
    public int getCount() {
        try {
            return mItems.size();
        }catch (Exception e){
            return 0;
        }
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
