package at.jku.se.decisiondocu.dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import at.jku.se.decisiondocu.R;
import at.jku.se.decisiondocu.activities.SearchNodeDetailsActivity_;
import at.jku.se.decisiondocu.asynctask.RestNetworkTasks;
import at.jku.se.decisiondocu.beans.adapters.AddDecisionTeamAdapter;
import at.jku.se.decisiondocu.restclient.client.model.Project;

/**
 * Created by Benjamin on 24.12.2015.
 *
 * Dialog for creating a new decision. It uses the AddDecisionTeamAdapter for displaying the projects.
 * The REST Call works asynchronously
 *
 */
@EFragment(R.layout.fragmentdialog_add)
public class CreateDecisionDialog extends DialogFragment {

    private static final int RESULT_TAKE_IMAGE = 0;
    Bitmap imageBitmap;

    @ViewById(R.id.teamchoosspinner)
    Spinner spinner;

    @ViewById(R.id.add_progress)
    ProgressBar bar;

    @ViewById(R.id.CreatDecLayout)
    LinearLayout layout;

    @ViewById(R.id.add_desc_name)
    EditText text;

    @ViewById(R.id.addDec_image_view)
    ImageView imageView;

    @Bean
    AddDecisionTeamAdapter mAdapter;

    @AfterViews
    void init() {
        getDialog().getWindow().setLayout(400, LinearLayout.LayoutParams.WRAP_CONTENT);
        spinner.setAdapter(mAdapter);
    }

    @Click(R.id.add_btn_submit)
    void submit() {
        Project project = null;
        try {
            project = new Project();
            project.setId(((Project) spinner.getSelectedItem()).getId());
            String decname = text.getText().toString();
            if (project == null || decname == null || decname.length() < 5) {
                text.setError(getString(R.string.invalidDecName));
                text.requestFocus();
                return;
            }

            // async create decision
            new RestNetworkTasks.CreateDecisionTask(bar, layout, getContext(), project, decname, imageBitmap) {
                @Override
                protected void onPostExecute(Integer success) {
                    super.onPostExecute(success);
                    if (success > 0) {
                        System.out.println(decisionid);
                        new SearchNodeDetailsActivity_.IntentBuilder_(getActivity())
                                .decisionId(this.decisionid)
                                .start();
                        close();
                    } else {
                        text.setError(getString(R.string.invalidDecName));
                        text.requestFocus();
                        spinner.requestFocus();
                    }
                }

            }.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.add_take_picture)
    void PictureBtnClick() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, RESULT_TAKE_IMAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @OnActivityResult(RESULT_TAKE_IMAGE)
    void PictureBtnResult(int resultCode, Intent data) {
        try {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
            }
        }catch (Exception e){}
    }

    private void close() {
        dismiss();
    }
}
