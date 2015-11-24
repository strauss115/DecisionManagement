package at.jku.se.decisiondocu.beans;

/**
 * Created by martin on 24.11.15.
 */
public class Team {

    private String mTeamImageUrl;
    private String mTeamName;
    private int mTeamDecisionCount;

    private boolean mImgDownloaded;

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

    public boolean isImgDownloaded() {
        return mImgDownloaded;
    }

    public void setImgDownloaded(boolean mImgDownloaded) {
        this.mImgDownloaded = mImgDownloaded;
    }

    @Override
    public String toString() {
        return "Team{" +
                "mTeamImageUrl='" + mTeamImageUrl + '\'' +
                ", mTeamName='" + mTeamName + '\'' +
                ", mTeamDecisionCount=" + mTeamDecisionCount +
                '}';
    }
}
