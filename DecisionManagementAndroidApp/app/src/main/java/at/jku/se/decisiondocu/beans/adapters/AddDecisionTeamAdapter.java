package at.jku.se.decisiondocu.beans.adapters;

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

import at.jku.se.decisiondocu.beans.RESTProjectFinder;
import at.jku.se.decisiondocu.beans.interfaces.TeamFinder;
import at.jku.se.decisiondocu.restclient.client.model.Project;
import at.jku.se.decisiondocu.views.CreateDecisionTeamItemView;
import at.jku.se.decisiondocu.views.CreateDecisionTeamItemView_;

/**
 * Created by martin on 24.11.15.
 *
 * Adapter, used for the Create Decision Dialog.
 * It contains a list of Project Objects and uses CreateDecisionTeamItemView.
 *
 */
@EBean
public class AddDecisionTeamAdapter extends BaseAdapter {

    private ProgressDialog mDialog;
    private List<Project> mItems;

    @Bean(RESTProjectFinder.class)
    TeamFinder mTeamFinder;

    @AfterInject
    void initAdapter() {
        mItems = new ArrayList<>();
        findAll();
    }

    @Background
    void findAll() {
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
        if (mDialog == null) {
            mDialog = new ProgressDialog(context);
            mDialog.setMessage("please wait...");
            mDialog.show();
        }
    }

    @UiThread
    void dismissDialog() {
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
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Project getItem(int position) {
        return mItems.get(position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CreateDecisionTeamItemView view;

        if (convertView == null) {
            view = CreateDecisionTeamItemView_.build(context, this);
        } else {
            view = (CreateDecisionTeamItemView) convertView;
        }

        view.bind(getItem(position));
        return view;
    }
}
