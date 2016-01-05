package at.jku.se.decisiondocu.asynctask;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

import at.jku.se.decisiondocu.login.SaveSharedPreference;
import at.jku.se.decisiondocu.restclient.RestClient;
import at.jku.se.decisiondocu.restclient.client.model.Decision;
import at.jku.se.decisiondocu.restclient.client.model.Document;
import at.jku.se.decisiondocu.restclient.client.model.NodeInterface;
import at.jku.se.decisiondocu.restclient.client.model.Project;
import at.jku.se.decisiondocu.restclient.client.model.RelationString;

/**
 * Created by Benjamin on 19.11.2015.
 */
public class RestNetworkTasks {

    /**
     * Class used for login a user asynchronously
     */
    public static abstract class UserLoginTask extends NetworkTask {

        private final long TOKEN_DURATION = 3600 * 1000; // one hour
        private final String mEmail;
        private final String mPassword;
        private String token;

        protected UserLoginTask(View progressBar, View viewToHide, Context context, String email, String password) {
            super(progressBar, viewToHide, context);
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            token = RestClient.getToken(mEmail, mPassword);
            if (token != null) {
                SaveSharedPreference.setUserEmail(context, mEmail);
                SaveSharedPreference.setUserPassword(context, mPassword);
                SaveSharedPreference.setUserToken(context, token);
                SaveSharedPreference.setUserTokenTimestamp(context, System.currentTimeMillis() + TOKEN_DURATION); // timestamp will expire in one hour
                return 1;
            }
            return 0;
        }
    }

    /**
     * Class used for register a user asynchronously
     */
    public static abstract class UserRegisterTask extends NetworkTask {

        private final String mfirstname;
        private final String mlastname;
        private final String mEmail;
        private final String mPassword;
        private final Bitmap mProfile;

        protected UserRegisterTask(View progressBar, View viewToHide, Context context,
                                   String firstname, String lastname, String email, String password, Bitmap profil) {
            super(progressBar, viewToHide, context);
            mfirstname = firstname;
            mlastname = lastname;
            mEmail = email;
            mPassword = password;
            mProfile = profil;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            if (RestClient.registerUser(mfirstname, mlastname, mEmail, mPassword, mProfile)) {
                return 1;
            }
            return 0;
        }
    }

    /**
     * Class used for creating a decision asynchronously
     */
    public static abstract class CreateDecisionTask extends NetworkTask {

        private final Project project;
        private final String name;
        private final Bitmap bitmap;
        protected long decisionid=-1;

        protected CreateDecisionTask(View progressBar, View viewToHide, Context context,
                                     Project project, String name, Bitmap bitmap) {
            super(progressBar, viewToHide, context);
            this.project = project;
            this.name = name;
            this.bitmap = bitmap;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            if (name == null || name.length() < 1 || project == null) {
                return 0;
            }
            Decision dec = new Decision();
            dec.setName(name);
            project.addRelation(RelationString.HASDECISION, dec, true);
            NodeInterface decision = RestClient.createDecision(project);

            if (decision == null) {
                return 0;
            }
            System.out.println(decision);
            try {
                decisionid = decision.getRelationships().get(RelationString.HASDECISION).get(0).getRelatedNode().getId();
                if (decisionid < 0) {
                    return 0;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
             if (bitmap != null) {
                try {
                     RestClient.saveDocument(bitmap, decisionid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return 1;
        }
    }

    /**
     * Class used for download a user's profile picture asynchronously
     */
    public static abstract class DownloadProfilPicture extends NetworkTask {

        private final long id;
        protected Bitmap image;
        protected ImageView imageView;

        protected DownloadProfilPicture(View progressBar, View viewToHide, Context context,
                                        long id, ImageView imageView) {
            super(progressBar, viewToHide, context);
            this.id = id;
            this.imageView = imageView;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            Bitmap bitmap = RestClient.downloadProfilPicture(id);
            if (bitmap == null) {
                return 0;
            }
            image = bitmap;
            return 1;
        }
    }

    /**
     * Class used for download a downloads asynchronously
     */
    public static abstract class DownloadDocument extends NetworkTask {

        protected final Document doc;
        protected File file;

        protected DownloadDocument(View progressBar, View viewToHide, Context context,
                                   Document doc) {
            super(progressBar, viewToHide, context);
            this.doc = doc;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            file = RestClient.downloadDocument(doc);
            if (file == null) {
                return 0;
            }
            return 1;
        }
    }
}
