package at.jku.se.decisiondocu.asynctask;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.sql.Timestamp;

import at.jku.se.decisiondocu.login.SaveSharedPreference;
import at.jku.se.decisiondocu.restclient.RestClient;
import at.jku.se.decisiondocu.restclient.client.model.Decision;
import at.jku.se.decisiondocu.restclient.client.model.Project;
import at.jku.se.decisiondocu.restclient.client.model.RelationString;

/**
 * Created by Benjamin on 19.11.2015.
 */
public class RestNetworkTasks {

    public static abstract class UserLoginTask extends NetworkTask {

        private final String mEmail;
        private final String mPassword;
        private String token;

        protected UserLoginTask(View progressBar, View viewToHide, Context context, String email, String password) {
            super(progressBar,viewToHide,context);
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            token = RestClient.getToken(mEmail,mPassword);
            if(token!=null){
                SaveSharedPreference.setUserEmail(context, mEmail);
                SaveSharedPreference.setUserPassword(context, mPassword);
                SaveSharedPreference.setUserToken(context, token);

                Timestamp original = new Timestamp(System.currentTimeMillis());
                Timestamp later = new Timestamp(System.currentTimeMillis() + 3600 * 1000);

                Log.d("timestamp", original.toString());
                Log.d("timestamp", later.toString());

                SaveSharedPreference.setUserTokenTimestamp(context, System.currentTimeMillis() + 3600 * 1000);
                return 1;
            }
            return 0;
        }
    }

    public static abstract class UserRegisterTask  extends NetworkTask {

        private final String mfirstname;
        private final String mlastname;
        private final String mEmail;
        private final String mPassword;
        private final Bitmap mProfile;

        protected UserRegisterTask(View progressBar, View viewToHide, Context context,
                         String firstname, String lastname, String email, String password, Bitmap profil) {
            super(progressBar,viewToHide,context);
            mfirstname=firstname;
            mlastname=lastname;
            mEmail = email;
            mPassword = password;
            mProfile = profil;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            if(RestClient.registerUser(mfirstname, mlastname, mEmail, mPassword, mProfile)){
                return 1;
            }
            return 0;
        }
    }

    public static abstract class CreateDecisionTask  extends NetworkTask {

        private final Project project;
        private final String name;

        protected CreateDecisionTask(View progressBar, View viewToHide, Context context,
                                   Project project, String name) {
            super(progressBar,viewToHide,context);
            this.project=project;
            this.name=name;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            if(name==null||name.length()<1||project==null){
                return 0;
            }
            Decision dec = new Decision();
            dec.setName(name);
            project.addRelation(RelationString.HASDECISION, dec, true);
            String decision = RestClient.createDecision(project);
            Log.d("Test", decision);
            if(decision==null){
                return 0;
            }
            return 1;
        }
    }

    public static abstract class DownloadProfilPicture  extends NetworkTask {

        private final long id;
        protected Bitmap image;
        protected ImageView imageView;

        protected DownloadProfilPicture(View progressBar, View viewToHide, Context context,
                                     long id, ImageView imageView) {
            super(progressBar,viewToHide,context);
            this.id=id;
            this.imageView=imageView;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            Bitmap bitmap =RestClient.downloadProfilPicture(id);
            if(bitmap==null) {
                return 0;
            }
            image = bitmap;
            return 1;
        }
    }
}
