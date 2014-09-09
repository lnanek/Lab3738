package com.thenewcircle.myquickblogger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;
import com.marakana.android.yamba.clientlib.YambaClient;


public class ViewTimelineActivity extends Activity {

    private static final int GET_TIMELINE_SUCCESS = 0;
    private static final int GET_TIMELINE_ERROR = 1;

    public class ViewHolder {
        public TextView usernameView;
        public TextView statusView;
    }

    public class StatusListAdapter extends ArrayAdapter<YambaClient.Status> {

        public StatusListAdapter(List<YambaClient.Status> items) {
            super(ViewTimelineActivity.this, android.R.layout.simple_expandable_list_item_1, items);
        }

        @Override
        public View getView(final int i, final View convertView, final ViewGroup viewGroup) {
            final YambaClient.Status message = getItem(i);

            final ViewHolder viewHolder;
            final ViewGroup layout;
            if(null != convertView) {
                layout = (ViewGroup) convertView;
                viewHolder = (ViewHolder) convertView.getTag();
            }
            else {
                viewHolder = new ViewHolder();
                layout = (ViewGroup) getLayoutInflater().inflate(R.layout.timeline_item, null);
                viewHolder.usernameView = (TextView) layout.findViewById(R.id.timelineItemUsername);
                viewHolder.statusView = (TextView) layout.findViewById(R.id.timelineItemStatus);
                layout.setTag(viewHolder);
            }

            viewHolder.usernameView.setText(message.getUser());
            viewHolder.statusView.setText(message.getMessage());

            return layout;
        }
    }

    // Handle messages on our UI thread where we can call methods on buttons, text views, etc..
    private Handler uiThreadHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(final Message msg) {
            Log.d("ViewTimelineActivity", "received get timeline result on main thread: " + msg.obj);

            if (isFinishing()) {
                return;
            }

            if(null != progressDialog) {
                progressDialog.dismiss();
                progressDialog = null;
            }

            switch(msg.what) {
                case GET_TIMELINE_SUCCESS:
                    final List<YambaClient.Status> items = (List<YambaClient.Status>) msg.obj;
                    showData(items);
                    return;
                case GET_TIMELINE_ERROR:
                    showRetryDialog();
                    return;
            }
        }
    };

    // Handle messages on our background thread where we can not lock up the user interface
    private HandlerThread backgroundThread = new HandlerThread("get timeline items");

    {
        backgroundThread.start();
    }

    private Handler backgroundThreadHandler = new Handler(backgroundThread.getLooper()) {
        @Override
        public void handleMessage(final Message msg) {
            Log.d("ViewTimelineActivity", "received get timeline request on background thread: " + msg.obj);

            YambaClient yc = new YambaClient("student", "password");
            try {
                final List<YambaClient.Status> items = yc.getTimeline(100);
                Message resultMessage = uiThreadHandler.obtainMessage(GET_TIMELINE_SUCCESS, items);
                uiThreadHandler.sendMessage(resultMessage);
            }
            catch(Exception e) {
                Log.d("ViewTimelineActivity", "error getting timeline", e);
                Message errorMessage = uiThreadHandler.obtainMessage(GET_TIMELINE_ERROR, e);
                uiThreadHandler.sendMessage(errorMessage);
            }
        }
    };

    private ListView timelineDisplay;

    private ProgressDialog progressDialog;

    private StatusDetailFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_timeline);
        timelineDisplay = (ListView) findViewById(R.id.timelineDisplay);
        getActionBar().setHomeButtonEnabled(true);

        fragment = (StatusDetailFragment) getFragmentManager().findFragmentById(R.id.viewer);
        Log.d("ViewTimelineActivity", "got fragment = " + fragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshTimelineItems();
    }

    public void showRetryDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).setMessage("Error getting timeline.").setNegativeButton("Cancel", null).setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, final int i) {
                refreshTimelineItems();
            }
        }).show();
    }

    private void refreshTimelineItems() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        backgroundThreadHandler.sendEmptyMessage(0);
    }

    private void showData(List<YambaClient.Status> items) {

        if ( null != items && !items.isEmpty() ) {
            fragment.showDetails(items.get(0));
        }

        timelineDisplay.setAdapter(new StatusListAdapter(items));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == android.R.id.home) {

            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
