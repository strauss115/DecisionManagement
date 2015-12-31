package at.jku.se.decisiondocu.restclient.client.model;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

import at.jku.se.decisiondocu.asynctask.RestNetworkTasks;

public class Project extends Node {

	@JsonIgnore
	public int getNrOfDecisions() {
		return nrOfDecisions;
	}
	@JsonIgnore
	public void setNrOfDecisions(int nrOfDecisions) {
		this.nrOfDecisions = nrOfDecisions;
	}

    @JsonIgnore
    public Bitmap getProfilePic() {
        if(profilePic!=null){
            return profilePic;
        }
        if(getRelationships().containsKey(at.jku.se.decisiondocu.restclient.client.DBStrings.RelationString.HAS_PICTURE)) {
            try {
                long picid = getRelationships().get(at.jku.se.decisiondocu.restclient.client.DBStrings.RelationString.HAS_PICTURE).get(0).getRelatedNode().getId();
                new RestNetworkTasks.DownloadProfilPicture(null, null, null, picid, null) {
                    @Override
                    protected void onPostExecute(Integer success) {
                        super.onPostExecute(success);
                        if (success > 0 && this.image != null) {
                            try {
                                setProfilePic(this.image);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return profilePic;
    }

    @JsonIgnore
    public void setProfilePic(Bitmap profilePic) {
        this.profilePic = profilePic;
    }

    @JsonIgnore
    private Bitmap profilePic;

    @JsonIgnore
	private int nrOfDecisions = -1;

	public Project(String name, User admin, String password) {
		super(name);
		this.addRelation(RelationString.PROJECTADMIN, admin, true);
		this.addDirectProperty(PropertyString.PASSWORD, password);
	}

	public Project() {
		super();
	}

	@JsonIgnore
	public String getPassword() {
		try{
		return super.getDirectProperties().get(PropertyString.PASSWORD);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@JsonIgnore
	public void setPassword(String password) {
		super.addDirectProperty(PropertyString.PASSWORD, password);
	}

	@Override
	public Map<String, String> getDirectProperties() {
		if (super.getDirectProperties() == null)
			return null;
		HashMap<String, String> result = new HashMap<String, String>(super.getDirectProperties());
		result.remove(PropertyString.PASSWORD);
		return result;
	}

    @JsonIgnore
    public void setImageView(ImageView imageView) {
        if(profilePic!=null){
            imageView.setImageBitmap(profilePic);
            return;
        }
        if(getRelationships().containsKey(at.jku.se.decisiondocu.restclient.client.DBStrings.RelationString.HAS_PICTURE)) {
            try {
                long picid = getRelationships().get(at.jku.se.decisiondocu.restclient.client.DBStrings.RelationString.HAS_PICTURE).get(0).getRelatedNode().getId();
                new RestNetworkTasks.DownloadProfilPicture(null, null, null, picid, imageView) {
                    @Override
                    protected void onPostExecute(Integer success) {
                        super.onPostExecute(success);
                        if (success > 0 && this.image != null) {
                            try {
                                setProfilePic(this.image);
                                imageView.setImageBitmap(this.image);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
