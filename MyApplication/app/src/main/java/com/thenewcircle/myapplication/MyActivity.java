package com.thenewcircle.myapplication;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.sql.SQLException;


public class MyActivity extends ListActivity {

    public static final int ACTIVITY_CREATE = Activity.RESULT_FIRST_USER;
    public static final int ACTIVITY_EDIT = ACTIVITY_CREATE + 1;

    public static final int INSERT_ID = Menu.FIRST;
    public static final int DELETE_ID = INSERT_ID + 1;

    private NotesDbAdapter mDbHelper;

    private int mNoteNumber = 1;

    private long mSelectedNoteId = -1;

    private SimpleCursorAdapter notes;

    private Cursor mNotesCursor;

    @Override
    protected void onCreate(final Bundle aSavedInstanceState) {
        super.onCreate(aSavedInstanceState);
        setContentView(R.layout.activity_my);

        mDbHelper = new NotesDbAdapter(this);
        try {
            mDbHelper.open();
        } catch (SQLException e) {
            Log.e("MyActivity", "Error opening DB", e);
        }

        fillData();

        registerForContextMenu(getListView());

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("MyActivity", "onItemClick position = " + position);
                final SQLiteCursor item = (SQLiteCursor) notes.getItem(position);

                Log.d("MyActivity", "onItemClick item = " + item);
                final long noteId = item.getLong(item.getColumnIndex("_id"));
                mSelectedNoteId = noteId;

                Log.d("MyActivity", "noteId = " + noteId);

                Cursor c = mNotesCursor;

                c.moveToPosition(position);
                Intent i = new Intent(MyActivity.this, EditNoteActivity.class);
                i.putExtra(NotesDbAdapter.KEY_ROWID, id);
                i.putExtra(NotesDbAdapter.KEY_TITLE, c.getString(
                        c.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
                i.putExtra(NotesDbAdapter.KEY_BODY, c.getString(
                        c.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
                startActivityForResult(i, ACTIVITY_EDIT);
            }
        });

        startActivity(new Intent(this, ReadActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);

        final MenuItem insertMenuItem =
                menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        insertMenuItem.setIcon(R.drawable.ic_launcher);

        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case INSERT_ID:
                createNote();
                return true;
            case DELETE_ID:
                if (-1 != mSelectedNoteId) {
                    mDbHelper.deleteNote(mSelectedNoteId);
                }
                fillData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createNote() {
        String noteName = "Note " + mNoteNumber++;
        mDbHelper.createNote(noteName, "");
        fillData();
    }

    private void fillData() {
        // Get all of the notes from the database and create the item list
        mNotesCursor = mDbHelper.fetchAllNotes();
        startManagingCursor(mNotesCursor);

        String[] from = new String[]{NotesDbAdapter.KEY_TITLE};
        int[] to = new int[]{R.id.itemTextView};

        // Now create an array adapter and set it to display using our row
        notes =
                new SimpleCursorAdapter(this, R.layout.notes_row, mNotesCursor, from, to);

        setListAdapter(notes);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("MyActivity", "onActivityResult requestCode = " + requestCode);

        if ( null != data ) {
            Bundle extras = data.getExtras();
            switch (requestCode) {
                case ACTIVITY_CREATE:
                    String title = extras.getString(NotesDbAdapter.KEY_TITLE);
                    String body = extras.getString(NotesDbAdapter.KEY_BODY);
                    mDbHelper.createNote(title, body);
                    fillData();
                    break;
                case ACTIVITY_EDIT:
                    Long mRowId = extras.getLong(NotesDbAdapter.KEY_ROWID);
                    if (mRowId != null) {
                        String editTitle = extras.getString(NotesDbAdapter.KEY_TITLE);
                        String editBody = extras.getString(NotesDbAdapter.KEY_BODY);
                        mDbHelper.updateNote(mRowId, editTitle, editBody);
                    }
                    fillData();
                    break;
            }
        }
    }
}
