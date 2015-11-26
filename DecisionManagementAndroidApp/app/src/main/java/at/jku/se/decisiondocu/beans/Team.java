package at.jku.se.decisiondocu.beans;

import android.graphics.Bitmap;

/**
 * Created by martin on 24.11.15.
 */
public class Team {

    private String mTeamImageUrl;
    private String mTeamName;
    private int mTeamDecisionCount;
    private Bitmap mBitmap;
    private boolean mIsFavourite;

    public Team() {}

    public Team(String name) {
        this.mTeamName = name;
    }

    public String getTeamImageUrl() {
        return mTeamImageUrl;
    }

    public void setTeamImageUrl(String mTeamImageUrl) {
        this.mTeamImageUrl = mTeamImageUrl;
    }

    public String getTeamName() {
        return mTeamName;
    }

    public void setTeamName(String mTeamName) {
        this.mTeamName = mTeamName;
    }

    public int getTeamDecisionCount() {
        return mTeamDecisionCount;
    }

    public void setTeamDecisionCount(int mTeamDecisionCount) {
        this.mTeamDecisionCount = mTeamDecisionCount;
    }

    @Override
    public String toString() {
        return "Team{" +
                ", mTeamName='" + mTeamName + '\'' +
                ", mTeamDecisionCount=" + mTeamDecisionCount +
                ", mBitmap=" + mBitmap +
                ", mIsFavourite=" + mIsFavourite +
                '}';
    }

    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public boolean isFavourite() {
        return mIsFavourite;
    }

    public void setFavourite(boolean mIsFavourite) {
        this.mIsFavourite = mIsFavourite;
    }
}
