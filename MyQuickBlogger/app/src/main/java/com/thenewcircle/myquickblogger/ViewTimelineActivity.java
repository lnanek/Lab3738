package com.thenewcircle.myquickblogger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import com.marakana.android.yamba.clientlib.YambaClient;


public class ViewTimelineActivity extends Activity {

    private TextView timelineDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_timeline);
        timelineDisplay = (TextView) findViewById(R.id.timelineDisplay);
        getActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshTimelineItems();
    }

    private void refreshTimelineItems() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("ViewTimelineActivity", "starting network call");

                YambaClient yc = new YambaClient("student", "password");
                try {
                    final List<YambaClient.Status> items = yc.getTimeline(10);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            StringBuilder timelineDisplayString = new StringBuilder();
                            for(YambaClient.Status item : items) {
                                timelineDisplayString.append(item.getMessage() + "\n");
                            }
                            timelineDisplay.setText(timelineDisplayString.toString());
                        }
                    });

                } catch (Exception e) {
                    Log.d("PostUpdateActivity", "error posting status", e);


                }
            }
        }) {

        }.start();



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {

            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
