package com.thenewcircle.myapplication;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by lnanek on 9/10/14.
 */
public class ExampleContentProvider extends ContentProvider {

    private NotesDbAdapter mDbHelper;

    @Override
    public boolean onCreate() {

       Log.d("ExampleContentProvider", "onCreate");

        mDbHelper = new NotesDbAdapter(getContext());
        try {
            mDbHelper.open();
        } catch (SQLException e) {
            Log.e("MyActivity", "Error opening DB", e);
            return false;
        }

        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
               Log.d("ExampleContentProvider", "query");


        if ( null != sortOrder ) {
            throw new IllegalArgumentException("sorting not supported");
        }


        return mDbHelper.query(projection);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
