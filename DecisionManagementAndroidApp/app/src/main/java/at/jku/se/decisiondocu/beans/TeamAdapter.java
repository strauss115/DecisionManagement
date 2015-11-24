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
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import at.jku.se.decisiondocu.views.TeamItemView;
import at.jku.se.decisiondocu.views.TeamItemView_;

/**
 * Created by martin on 24.11.15.
 */
@EBean
public class TeamAdapter extends BaseAdapter {

    private List<Team> mItems;

    @AfterInject
    void initAdapter() {
        mItems = new ArrayList<>();

        Team t1 = new Team("Team 1");
        t1.setTeamDecisionCount(21);
        t1.setTeamImageUrl("http://www.keenthemes.com/preview/metronic/theme/assets/global/plugins/jcrop/demos/demo_files/image1.jpg");
        Team t2 = new Team("Team 2");
        t2.setTeamDecisionCount(2);
        t2.setTeamImageUrl("http://www.menucool.com/slider/jsImgSlider/images/image-slider-2.jpg");

        mItems.add(t1);
        mItems.add(t2);
    }

    @RootContext
    Context context;

    private void getData() {

    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Team getItem(int position) {
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
}
