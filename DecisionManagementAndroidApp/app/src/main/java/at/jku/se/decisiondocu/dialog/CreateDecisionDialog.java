package at.jku.se.decisiondocu.dialog;

import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import at.jku.se.decisiondocu.R;
import at.jku.se.decisiondocu.beans.TeamAdapter;

/**
 * Created by Benjamin on 24.12.2015.
 */
@EFragment(R.layout.fragmentdialog_add)
public class CreateDecisionDialog extends DialogFragment {

    @ViewById(R.id.teamchoosspinner)
    Spinner spinner;

    @Bean
    TeamAdapter mAdapter;

    @AfterViews
    void init() {
        getDialog().getWindow().setLayout(400,LinearLayout.LayoutParams.WRAP_CONTENT);
        spinner.setAdapter(mAdapter);

    }

    @Click(R.id.add_btn_submit)
    void submit() {
        Log.d("btn", "submit");
    }



}
