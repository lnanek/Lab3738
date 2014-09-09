package com.thenewcircle.myquickblogger;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * @author Lance Nanek (c07793)
 */
public class DetailViewerActivity extends Activity {

    public static final String TIMELINE_ITEM_INDEX_EXTRA =
            DetailViewerActivity.class.getName() + ".TIMELINE_ITEM_INDEX_EXTRA";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        final int index = extras.getInt(TIMELINE_ITEM_INDEX_EXTRA, -1);
        if (-1 == index) {
            Log.w("DetailViewerActivity", "No index to show supplied.");
            finish();
            return;
        }

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            StatusDetailFragment fragment = new StatusDetailFragment();

            StatusDetailFragment detail = StatusDetailFragment.newInstance(index);

            fragmentTransaction.add(android.R.id.content, fragment);
            fragmentTransaction.commit();
        }
    }
}
