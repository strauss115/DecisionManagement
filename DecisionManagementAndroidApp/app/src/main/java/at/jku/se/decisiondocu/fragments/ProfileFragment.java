package at.jku.se.decisiondocu.fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import at.jku.se.decisiondocu.R;
import at.jku.se.decisiondocu.login.SaveSharedPreference;
import at.jku.se.decisiondocu.restclient.RestClient;
import at.jku.se.decisiondocu.restclient.client.model.User;

/**
 * Created by martin on 03.01.16.
 */
@EFragment(R.layout.fragment_profile)
public class ProfileFragment extends Fragment {

    protected ProgressDialog mDialog;

    @ViewById(R.id.profile_name)
    TextView tv_name;

    @ViewById(R.id.profile_email)
    TextView tv_email;

    @ViewById(R.id.profile_image)
    ImageView imageView;

    @AfterViews
    void init() {
        load();
    }

    @Background
    public void load() {
        showDialog();
        User u = RestClient.getUser(SaveSharedPreference.getUserEmail(getContext()));
        Bitmap bitmap = null;
        if (u != null) {
            bitmap = RestClient.downloadProfilPicture(u.getId());
        }
        update(u, bitmap);
        dismissDialog();
    }

    @UiThread
    void update(User profile, Bitmap bitmap) {
        if (profile != null) {
            tv_name.setText(profile.getName() + " " + profile.getLastname());
            tv_email.setText(profile.getEmail());
        }
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }

    @UiThread
    void showDialog() {
        Log.d("dialog", "showing");
        if (mDialog == null) {
            mDialog = new ProgressDialog(getActivity());
            mDialog.setMessage("please wait...");
            mDialog.show();
        }
    }

    @UiThread
    void dismissDialog() {
        Log.d("dialog", "hiding");
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
}
