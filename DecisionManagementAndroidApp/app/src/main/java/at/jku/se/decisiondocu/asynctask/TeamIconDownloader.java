package at.jku.se.decisiondocu.asynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;

import at.jku.se.decisiondocu.beans.Team;

/**
 * Created by martin on 24.11.15.
 */
public class TeamIconDownloader extends AsyncTask<String, Void, Bitmap> {

    private Team mTeam;
    private OnAsyncTaskFinished mCallback;

    public TeamIconDownloader(Team team, OnAsyncTaskFinished callback) {
        mTeam = team;
        mCallback = callback;
    }

    protected Bitmap doInBackground(String... urls) {
        String mUrl = mTeam.getTeamImageUrl();
        Bitmap mIcon = null;
        try {
            InputStream in = new java.net.URL(mUrl).openStream();
            mIcon = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon;
    }

    protected void onPostExecute(Bitmap result) {
        if (result != null) {
            mTeam.setBitmap(result);
        }
        mCallback.finished();
    }
}
