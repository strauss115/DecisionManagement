package at.jku.se.decisiondocu.views;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import at.jku.se.decisiondocu.R;
import at.jku.se.decisiondocu.restclient.client.model.Project;

/**
 * Created by martin on 23.11.15.
 */
@EViewGroup(R.layout.viewgroup_adddecisionteam)
public class CreateDecisionTeamItemView extends LinearLayout {

    @ViewById(R.id.team_name1)
    TextView mTeamName;

    @ViewById(R.id.imageView1)
    ImageView mImageView;

    private BaseAdapter mAdapter;
    private Project mTeam;

    public CreateDecisionTeamItemView(Context context) {
        super(context);
    }

    public CreateDecisionTeamItemView(Context context, BaseAdapter adapter) {
        this(context);
        this.mAdapter = adapter;
    }

    public void bind(Project item) {
        mTeam = item;
        mTeamName.setText(item.getName());

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
}
