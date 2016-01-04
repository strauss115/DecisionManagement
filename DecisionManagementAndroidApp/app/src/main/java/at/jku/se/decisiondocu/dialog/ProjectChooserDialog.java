package at.jku.se.decisiondocu.dialog;

import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import at.jku.se.decisiondocu.R;
import at.jku.se.decisiondocu.beans.adapters.TeamAdapter;
import at.jku.se.decisiondocu.beans.adapters.TeamChooserAdapter;
import at.jku.se.decisiondocu.restclient.RestClient;
import at.jku.se.decisiondocu.restclient.client.model.Project;

/**
 * Created by martin on 28.12.15.
 *
 * Dialog for adding a user to a project group
 * The REST Call works asynchronously with the @Background annotation
 *
 */
@EFragment(R.layout.fragmentdialog_addproject)
public class ProjectChooserDialog extends DialogFragment {

    @ViewById(R.id.team_chooser_spinner)
    Spinner spinner;

    @ViewById(R.id.add_team_password)
    EditText password;

    @Bean(TeamChooserAdapter.class)
    TeamAdapter mAdapter;

    @AfterViews
    void init() {
        getDialog().getWindow().setLayout(400, LinearLayout.LayoutParams.WRAP_CONTENT);
        spinner.setAdapter(mAdapter);

    }

    @Click(R.id.add_team_submit)
    void submit() {

        int id = spinner.getSelectedItemPosition();
        Project p = mAdapter.getItem(id);

        addUserToProject(p.getId() + "", password.getText().toString());
    }

    @Background
    void addUserToProject(String id, String pw) {
        boolean b = RestClient.addUserToProject(id, pw);
        result(b);
    }

    @UiThread
    void result(boolean success) {
        if (success) {
            Log.d("btn", "success");
        } else {
            Log.e("btn", "failure");
        }
        dismiss();
    }
}
