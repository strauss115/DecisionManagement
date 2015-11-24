package at.jku.se.decisiondocu.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;
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

    public TeamItemView(Context context) {
        super(context);
    }

    public TeamItemView(Context context, BaseAdapter adapter) {
        this(context);
    }

    public void bind(Team item) {
        Log.i("bind called", item.toString());
        mTeamName.setText(item.getTeamName());
        mTeamDescCnt.setText((String.valueOf(item.getTeamDecisionCount())));

        if (!item.isImgDownloaded()) {
            new DownloadImageTask(mImageView)
                    .execute(item.getTeamImageUrl());
            item.setImgDownloaded(true);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
