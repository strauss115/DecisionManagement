package at.jku.se.decisiondocu.dialog;

import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import at.jku.se.decisiondocu.R;
import at.jku.se.decisiondocu.asynctask.RestNetworkTasks;
import at.jku.se.decisiondocu.beans.AddDecisionTeamAdapter;
import at.jku.se.decisiondocu.restclient.client.model.Project;

/**
 * Created by Benjamin on 24.12.2015.
 */
@EFragment(R.layout.fragmentdialog_add)
public class CreateDecisionDialog extends DialogFragment {

    @ViewById(R.id.teamchoosspinner)
    Spinner spinner;

    @ViewById(R.id.add_progress)
    ProgressBar bar;

    @ViewById(R.id.CreatDecLayout)
    LinearLayout layout;

    @ViewById(R.id.add_desc_name)
    EditText text;

    @Bean
    AddDecisionTeamAdapter mAdapter;

    @AfterViews
    void init() {
        getDialog().getWindow().setLayout(400,LinearLayout.LayoutParams.WRAP_CONTENT);
        spinner.setAdapter(mAdapter);

    }

    @Click(R.id.add_btn_submit)
    void submit() {
        Log.d("btn", "submit");
        Project project = null;
        try{
            project = new Project();
            project.setId(((Project)spinner.getSelectedItem()).getId());
            String decname = text.getText().toString();
            if(project == null||decname==null || decname.length()<5){
                text.setError(getString(R.string.invalidDecName));
                text.requestFocus();
                return;
            }

            new RestNetworkTasks.CreateDecisionTask(bar,layout,getContext(),project,decname){
                @Override
                protected void onPostExecute(Integer success) {
                    super.onPostExecute(success);
                    if (success>0) {
                        close();
                    } else {
                        text.setError(getString(R.string.invalidDecName));
                        text.requestFocus();
                        spinner.requestFocus();
                    }
                }

            }.execute();

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void close(){
        dismiss();
    }



}
