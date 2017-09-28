package com.example.tomek.notepad;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;

public class SaveOrUpdateNoteTask extends AsyncTask<Note, Void, Boolean> {

    private final Context mCallingActivity;
    private final DatabaseHandler mDbHandler;

    public SaveOrUpdateNoteTask(Context context) {
        mCallingActivity = context;
        mDbHandler = new DatabaseHandler(context);
    }

    @Override
    protected Boolean doInBackground(Note... params) {

        Note note = params[0];

        // If new note
        if (note.getId() == -1) {
            mDbHandler.createNote(note);
            return true;
        } else {
            mDbHandler.updateNote(note);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {

        boolean createdNewNote = result;

        ArrayList<Note> allNotes = mDbHandler.getAllNotesAsArrayList();
        if (createdNewNote) {
            Toast.makeText(mCallingActivity, mCallingActivity.getString(R.string.toast_note_created), Toast.LENGTH_SHORT).show();
            MainActivity.noteAdapter.add(allNotes.get(mDbHandler.getNoteCount() - 1));
        } else {
            Toast.makeText(mCallingActivity, mCallingActivity.getString(R.string.toast_note_updated), Toast.LENGTH_SHORT).show();
        }

        //TODO Get rid of these statics
        MainActivity.noteAdapter.setData(allNotes);
        MainActivity.noteAdapter.notifyDataSetChanged();
    }
}
