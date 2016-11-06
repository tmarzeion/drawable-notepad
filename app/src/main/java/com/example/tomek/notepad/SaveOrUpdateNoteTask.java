package com.example.tomek.notepad;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;

public class SaveOrUpdateNoteTask extends AsyncTask<Note, Void, Void> {

    private final Activity mCallingActivity;
    private final DatabaseHandler mDbHandler;
    private final boolean mIsUpdating;

    /**
     * Custom constructor
     * @param callingActivity used to make Toasts/Dialogs on it
     * @param databaseHandler database handler with calling activity context
     * @param isUpdating boolean that is used to see if note is new or updated
     */
    public SaveOrUpdateNoteTask(Activity callingActivity, DatabaseHandler databaseHandler, boolean isUpdating) {
        mCallingActivity = callingActivity;
        mDbHandler = databaseHandler;
        mIsUpdating = isUpdating;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(Note... params) {

        if (mIsUpdating) {
            mDbHandler.updateNote(params[0]);
        }
        else {
            mDbHandler.createNote(params[0]);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        ArrayList<Note> allNotes = mDbHandler.getAllNotesAsArrayList();
        if (mIsUpdating) {
            Toast.makeText(mCallingActivity, mCallingActivity.getString(R.string.toast_note_updated), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mCallingActivity, mCallingActivity.getString(R.string.toast_note_created), Toast.LENGTH_SHORT).show();
            MainActivity.noteAdapter.add(allNotes.get(mDbHandler.getNoteCount() - 1));
        }
        MainActivity.noteAdapter.setData(allNotes);
        MainActivity.noteAdapter.notifyDataSetChanged();
    }
}
