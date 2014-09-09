package com.thenewcircle.myquickblogger;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;

/**
 * Created by lnanek on 9/8/14.
 */
public class PostUpdateTask extends AsyncTask<String, Void, String> {

    public static final boolean LOG = false;

    private PostUpdateActivity activity;

    public PostUpdateTask(PostUpdateActivity activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        if ( LOG ) {
            Log.d("PostUpdateActivity", "starting network call");
        }

        YambaClient yc = new YambaClient("student", "password");
        try {
            yc.postStatus(params[0]);
            return "Posted!";

        } catch (Exception e) {
            Log.d("PostUpdateActivity", "error posting status", e);

            return "Error posting.";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        activity.showUserTaskResult("asdasd");
        Toast.makeText(activity, result, Toast.LENGTH_LONG).show();
    }
}
