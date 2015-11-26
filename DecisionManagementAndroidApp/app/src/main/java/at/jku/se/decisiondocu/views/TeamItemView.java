package at.jku.se.decisiondocu.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.io.InputStream;

import at.jku.se.decisiondocu.R;
import at.jku.se.decisiondocu.beans.Team;

/**
 * Created by martin on 23.11.15.
 */
@EViewGroup(R.layout.viewgroup_team)
public class TeamItemView extends LinearLayout {

    @ViewById(R.id.team_name)
    TextView mTeamName;

    @ViewById(R.id.team_desc_cnt)
    TextView mTeamDescCnt;

    @ViewById(R.id.imageView)
    ImageView mImageView;

    @ViewById(R.id.team_favourite)
    CheckBox mTeamFavourite;

    private BaseAdapter mAdapter;
    private Team mTeam;

    public TeamItemView(Context context) {
        super(context);
    }

    public TeamItemView(Context context, BaseAdapter adapter) {
        this(context);
        this.mAdapter = adapter;
    }

    public void bind(Team item) {
        mTeam = item;
        mTeamName.setText(item.getTeamName());
        mTeamDescCnt.setText((String.valueOf(item.getTeamDecisionCount())));
        if (item.getBitmap() != null) {
            mImageView.setImageBitmap(item.getBitmap());
        }
        mTeamFavourite.setChecked(item.isFavourite());

        setCheckboxOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                notifyTeamOnChanges();
            }
        });

    }

    private void notifyTeamOnChanges() {
        if (mTeam != null) {
            mTeam.setFavourite(mTeamFavourite.isChecked());
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setCheckboxOnClickListener(OnClickListener listener) {
        if (mTeamFavourite != null) {
            mTeamFavourite.setOnClickListener(listener);
        }
    }
}
