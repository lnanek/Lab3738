package com.thenewcircle.myquickblogger;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;


public class PostUpdateActivity extends Activity {

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

        viewTimelineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.google.com"));
                startActivity(intent);
            }
        });

        postUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String textUserEntered = statusInput.getText().toString();
                postUpdateTask = new PostUpdateTask(PostUpdateActivity.this);
                postUpdateTask.execute(textUserEntered);
            }
        });
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
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "I started", Toast.LENGTH_LONG).show();
        Log.d("PostUpdateActivity", "I started");
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

            final Intent intent = new Intent(this, ViewTimelineActivity.class);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
