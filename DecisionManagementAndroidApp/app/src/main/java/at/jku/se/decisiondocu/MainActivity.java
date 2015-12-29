package at.jku.se.decisiondocu;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import at.jku.se.decisiondocu.asynctask.RestNetworkTasks;
import at.jku.se.decisiondocu.dialog.CreateDecisionDialog_;
import at.jku.se.decisiondocu.fragments.ChatFragment_;
import at.jku.se.decisiondocu.fragments.SearchFragment_;
import at.jku.se.decisiondocu.fragments.TeamFragment_;
import at.jku.se.decisiondocu.login.SaveSharedPreference;
import at.jku.se.decisiondocu.restclient.RestClient;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    public static MainActivity Instance;

    //Test Commit

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.container)
    public ViewPager mViewPager;

    @ViewById(R.id.tabs)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        int res = SaveSharedPreference.isValidUser(this);
        switch (res) {
            case SaveSharedPreference.RESULT_OK:
                RestClient.accessToken = SaveSharedPreference.getUserToken(this);
                break;
            case SaveSharedPreference.RESULT_TOKEN_EXPIRED:
                new RestNetworkTasks.UserLoginTask(
                        null, null, getBaseContext(),
                        SaveSharedPreference.getUserEmail(this),
                        SaveSharedPreference.getUserPass(this)
                ){
                    @Override
                    protected void onPostExecute(Integer success) {
                        super.onPostExecute(success);
                        if (success>0) {
                            Intent intent = new Intent(getBaseContext(),MainActivity_.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }.execute();
                RestClient.accessToken = SaveSharedPreference.getUserToken(this);
                break;
            case SaveSharedPreference.RESULT_NOK:
                Intent intent = new Intent(this, LoginActivity_.class);
                startActivity(intent);
                finish();
                break;
            default:
                Log.e("Error", res + " not defined in switch!");
                break;

        }
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    protected void init() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout.setupWithViewPager(mViewPager);
        Instance = this;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        if (id == R.id.action_logout) {
            SaveSharedPreference.logout(this);
            Intent intent = new Intent(this, LoginActivity_.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    //Eventuell Fenster indem man schnell eine Enscheidung erstellen kann
    @Click(R.id.fab)
    void fab_pressed(){
        CreateDecisionDialog_ creatdialog = new CreateDecisionDialog_();
        creatdialog.show(getSupportFragmentManager(), "Create new Decision");
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position) {
                case 0:
                    return new TeamFragment_.FragmentBuilder_().build();
                case 1:
                    return new SearchFragment_.FragmentBuilder_().build();
                case 2:
                    return new ChatFragment_.FragmentBuilder_().build();
                default:
                    return MainActivity_.PlaceholderFragment_.builder().arg("section_number",position+1).build();
            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Projects";
                case 1:
                    return "Search";
                case 2:
                    return "Chat";
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    @EFragment(R.layout.fragment_main)
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        @ViewById(R.id.section_label)
        TextView textView;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        /*public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }*/

       @AfterViews
       protected void init() {
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        }
    }
}
