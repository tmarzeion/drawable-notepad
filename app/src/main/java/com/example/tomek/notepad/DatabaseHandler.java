package com.example.tomek.notepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.Spannable;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseHandler class used for Creating, Accessing and Modifying SQLite Database
 */
public class DatabaseHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notepadDatabase";
    private static final String TABLE_NOTES = "notes";
    private static final String KEY_ID = "id";
    private static final String KEY_SPANNABLE_NOTE = "serializedSpannableNote";
    private static final String KEY_IMAGE = "image";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_SPANNABLE_NOTE + " TEXT, "
                + KEY_IMAGE + " BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    /**
     * Method used to clear notes table
     */
    public void clearAllNotes() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NOTES);
    }

    /**
     * Method used to put Note object into Database
     * @param note Note object to put into DB
     */
    public void createNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        String spannableAsHtml = Html.toHtml(note.getSpannable());
        ContentValues values = new ContentValues();
        values.put(KEY_SPANNABLE_NOTE, spannableAsHtml);
        values.put(KEY_IMAGE, BitmapConverter.getBytes(note.getImage()));
        db.insert(TABLE_NOTES, null, values);
        db.close();
    }

    /**
     * Method used to get specified Note from Database
     * @param id KEY_ID of Note to get from Database
     * @return Note object with specified KEY_ID
     */
    public Note getNote(int id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTES, new String[]{KEY_ID, KEY_SPANNABLE_NOTE, KEY_IMAGE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String spannableAsHtml = cursor.getString(1);
        // :)))))
        Spannable spannable = (Spannable) Html.fromHtml(Html.toHtml(Html.fromHtml(spannableAsHtml)));

        if (spannable.length() >= 2) {
            spannable = (Spannable) spannable.subSequence(0, spannable.length() - 2);
        }

        Bitmap image = BitmapConverter.getImage(cursor.getBlob(2));

        db.close();
        cursor.close();
        return new Note(id, spannable, image);
    }

    /**
     * Method used to delete specified Note from Database
     * @param note Note to delete
     */
    public void deleteNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NOTES, KEY_ID + "=?", new String[]{String.valueOf(note.getId())});
        db.close();
    }

    /**
     * Method used to get count of notes in Database
     * @return count of notes in Database
     */
    public int getNoteCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES, null);
        int result = cursor.getCount();

        cursor.close();
        db.close();
        return result;
    }

    /**
     * Method used to update Note's text/format
     * @param note Note to update
     * @return updated Note
     */
    public int updateNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();

        String spannableAsHtml = Html.toHtml(note.getSpannable());

        ContentValues values = new ContentValues();
        values.put(KEY_IMAGE, BitmapConverter.getBytes(note.getImage()));
        values.put(KEY_SPANNABLE_NOTE, spannableAsHtml);

        return db.update(TABLE_NOTES, values, KEY_ID + "=?", new String[]{String.valueOf(note.getId())});
    }

    /**
     * Method used to get all notes in Database
     * @return ArrayList of Notes, containing all notes in Database
     */
    public List<Note> getAllNotesAsArrayList() {
        ArrayList<Note> notes = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note(Integer.parseInt(cursor.getString(0)),
                        (Spannable) Html.fromHtml(cursor.getString(1)),
                        BitmapConverter.getImage(cursor.getBlob(2)));
                notes.add(note);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return notes;
    }

    /**
     * Method used to get all notes in Database
     * @return Array of Notes, containing all notes in Database
     */
    public Note[] getAllNotesAsArray() {
        ArrayList<Note> notes = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note(Integer.parseInt(cursor.getString(0)),
                        (Spannable) Html.fromHtml(cursor.getString(1)),
                        BitmapConverter.getImage(cursor.getBlob(2)));
                notes.add(note);
            }
            while (cursor.moveToNext());
        }

        Note[] result = new Note[notes.size()];

        for (int i = 0; i < notes.size(); i++) {
            result[i] = notes.get(i);
        }

        cursor.close();
        return result;
    }
}
