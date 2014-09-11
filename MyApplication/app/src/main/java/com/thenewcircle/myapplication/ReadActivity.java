package com.thenewcircle.myapplication;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by lnanek on 9/10/14.
 */
public class ReadActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TextView resultsView = new TextView(this);
        resultsView.setText("Results:");
        setContentView(resultsView);

        final Uri uri = Uri.parse(
                "content://com.thenewcircle.myapplication.MyExampleAuth/test");
        Cursor c = getContentResolver().query(uri,
                null, null, null, null);

        while(c.moveToNext()) {
            Log.d("ReadActivity", "got row: " + c);

            final String title =
                    c.getString(c.getColumnIndex(NotesDbAdapter.KEY_TITLE));
            resultsView.append("\n" + title);
        }
    }
}
