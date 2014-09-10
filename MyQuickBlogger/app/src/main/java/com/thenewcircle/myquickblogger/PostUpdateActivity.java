package com.thenewcircle.myquickblogger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.marakana.android.yamba.clientlib.YambaClient;


public class PostUpdateActivity extends Activity implements View.OnClickListener {

    private BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final boolean disconnected = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

            Log.d("ConnectionReceiver", "onReceive received intent = " + intent
                    + " disconnected = " + disconnected);

            postUpdateButton.setEnabled(!disconnected);
        }
    };

    Handler handler = new Handler();

    Button postUpdateButton;

    Button viewTimelineButton;

    EditText statusInput;

    PostUpdateTask postUpdateTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_update);

        postUpdateButton = (Button) findViewById(R.id.postUpdateButton);
        viewTimelineButton = (Button) findViewById(R.id.viewTimelineButton);

        statusInput = (EditText) findViewById(R.id.statusInput);

        viewTimelineButton.setOnClickListener(this);

        postUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String textUserEntered = statusInput.getText().toString();
                postUpdateTask = new PostUpdateTask(PostUpdateActivity.this);
                postUpdateTask.execute(textUserEntered);
            }
        });


        final TextView resultDisplay = (TextView)
                findViewById(R.id.resultDisplay);

        //Create your request queue once
        RequestQueue queue = Volley.newRequestQueue(this);


        // Submit requests
        String url ="http://yamba.marakana.com/api/statuses/friends_timeline.xml";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        resultDisplay.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PostUpdateActivity", "error getting page", error);
                resultDisplay.setText("That didn't work!");
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //return super.getHeaders();

                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s","student","password");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };


        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    @Override
    public void onClick(final View view) {
        final Intent intent = new Intent("com.thenewcircle.myquickblogger.VIEW_BLOG_TIMELINE");
        startActivity(intent);
    }

    public void showRetryDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Error submitting post.")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {
                        updateBlog(statusInput.getText().toString());
                    }
                })
                .show();
    }

    public void showUserTaskResult(final String result) {

    }

    private void updateBlog(final String textUserEntered) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("PostUpdateActivity", "starting network call");

                YambaClient yc = new YambaClient("student", "password");
                try {
                    yc.postStatus(textUserEntered);

                } catch (Exception e) {
                    Log.d("PostUpdateActivity", "error posting status", e);
                    Toast.makeText(PostUpdateActivity.this,
                            "error post", Toast.LENGTH_LONG).show();
                }
            }
        }) {

        }.start();



    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "I resumed", Toast.LENGTH_LONG).show();
        Log.d("PostUpdateActivity", "I resumed");

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        postUpdateButton.setEnabled(isConnected);

        final IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        registerReceiver(connectivityReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(connectivityReceiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (null != postUpdateTask) {
            postUpdateTask.cancel(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            final Intent intent = new Intent("com.thenewcircle.myquickblogger.VIEW_BLOG_TIMELINE");
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
