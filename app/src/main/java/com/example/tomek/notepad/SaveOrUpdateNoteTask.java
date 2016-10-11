package com.example.tomek.notepad;

import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class SaveOrUpdateNoteTask extends AsyncTask<Note, Void, Void> implements Application.ActivityLifecycleCallbacks {

    private final Activity mCallingActivity;
    private final DatabaseHandler mDbHandler;
    private final boolean mIsUpdating;
    private Activity currentActivity = null;


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
        callingActivity.getApplication().registerActivityLifecycleCallbacks(this);
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

        if (mIsUpdating) {
            Toast.makeText(mCallingActivity, mCallingActivity.getString(R.string.toast_note_updated), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mCallingActivity, mCallingActivity.getString(R.string.toast_note_created), Toast.LENGTH_SHORT).show();
        }

        updateNoteListView();

    }

    private void updateNoteListView(){
        if (currentActivity != null && currentActivity instanceof MainActivity){
            ArrayList<Note> allNotes = mDbHandler.getAllNotesAsArrayList();
            Note newNote = null;
            if (!mIsUpdating){
                newNote = allNotes.get(mDbHandler.getNoteCount() - 1);
            }
            ((MainActivity) currentActivity).setListViewData(allNotes, newNote);
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

    @Override
    public void onActivityStarted(Activity activity) {}

    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {}

    @Override
    public void onActivityStopped(Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

    @Override
    public void onActivityDestroyed(Activity activity) {}
}
