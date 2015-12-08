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
import java.util.List;

import at.jku.se.decisiondocu.R;
import at.jku.se.decisiondocu.beans.Team;
import at.jku.se.decisiondocu.restclient.RestClient;
import at.jku.se.decisiondocu.restclient.client.api.DecisionApi;
import at.jku.se.decisiondocu.restclient.client.model.Decision;
import at.jku.se.decisiondocu.restclient.client.model.Project;

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
    private Project mTeam;

    public TeamItemView(Context context) {
        super(context);
    }

    public TeamItemView(Context context, BaseAdapter adapter) {
        this(context);
        this.mAdapter = adapter;
    }

    public void bind(Project item) {
        mTeam = item;
        mTeamName.setText(item.getName());

        if (item.getNrOfDecisions() == -1) {
            DecisionApi api = new DecisionApi();
            api.setBasePath("http://192.168.0.15:8080/DecisionDocu/api");
            try {
                List<Decision> list = api.getByProjectName(RestClient.accessToken, item.getId());
                if (list != null) {
                    item.setNrOfDecisions(list.size());
                } else {
                    item.setNrOfDecisions(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
                item.setNrOfDecisions(0);
            }
        }

        mTeamDescCnt.setText("Decisions: " + item.getNrOfDecisions());


        /*if (item.getBitmap() != null) {
            mImageView.setImageBitmap(item.getBitmap());
        }
        mTeamFavourite.setChecked(item.isFavourite());
*/
        setCheckboxOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                notifyTeamOnChanges();
            }
        });

    }

    private void notifyTeamOnChanges() {
        /*
        if (mTeam != null) {
            mTeam.setFavourite(mTeamFavourite.isChecked());
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        */
    }

    public void setCheckboxOnClickListener(OnClickListener listener) {
        if (mTeamFavourite != null) {
            mTeamFavourite.setOnClickListener(listener);
        }
    }
}
