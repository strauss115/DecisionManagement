package at.jku.se.decisiondocu.beans;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fasterxml.jackson.databind.deser.Deserializers;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import at.jku.se.decisiondocu.views.ListItemView;
import at.jku.se.decisiondocu.views.ListItemView_;
import at.jku.se.decisiondocu.views.TeamItemView;
import at.jku.se.decisiondocu.views.TeamItemView_;

/**
 * Created by martin on 24.11.15.
 */
@EBean
public class TeamAdapter extends BaseAdapter {

    private List<String> mItems;

    @AfterInject
    void initAdapter() {
        mItems = new ArrayList<>();
        mItems.add("Team xyz");
        mItems.add("Team 334");
    }

    @RootContext
    Context context;

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
        TeamItemView view;

        if (convertView == null) {
            view = TeamItemView_.build(context, this);
        } else {
            view = (TeamItemView) convertView;
        }

        view.bind((String)getItem(position));
        return view;
    }
}
