package at.jku.se.decisiondocu.fragments;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Date;

import at.jku.se.decisiondocu.R;

/**
 * Created by martin on 27.11.15.
 */
@EFragment(R.layout.fragment_add)
public class AddFragment extends Fragment {

    @ViewById(R.id.add_author)
    TextView mAuthor;

    @ViewById(R.id.add_date)
    TextView mDate;

    @AfterViews
    void init() {
        mAuthor.setText("Max");
        mDate.setText("yyyy-mm-dd");
    }

    @Click(R.id.add_btn_submit)
    void submit() {
        Log.d("btn", "submit");
    }
}
