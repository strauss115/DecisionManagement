package at.jku.se.decisiondocu.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

/**
 * Created by Benjamin on 19.11.2015.
 */
public abstract class NetworkTask  extends AsyncTask<Void, Void, Integer> {

    protected View progressBar;
    protected View viewToHide;
    protected Context context;

    /**
     * Default constructor
     */
    public NetworkTask(){}

    /**
     * Constructor
     * @param progressBar
     * @param viewToHide
     * @param context
     */
    public NetworkTask(View progressBar, View viewToHide, Context context){
        this.progressBar=progressBar;
        this.viewToHide=viewToHide;
        this.context=context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPreExecute(){
        showProgress(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostExecute(Integer success) {
        showProgress(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCancelled() {
        showProgress(false);
    }

    /**
     * {@inheritDoc}
     */
    public void showProgress(boolean progres){
        ProgressbarTransaction.showProgress(progres, progressBar, viewToHide, context);
    }

}
