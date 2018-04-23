package com.example.tomek.notepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * DatabaseHandler class used for Creating, Accessing and Modifying SQLite Database
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // TODO: Try not to exec SQL directly, prevent from SQL injects

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "notepadDatabase";
    private static final String TABLE_NOTES = "notes";
    private static final String KEY_ID = "id";
    private static final String KEY_NOTE_TITLE = "noteTitle";
    private static final String KEY_SPANNABLE_NOTE = "serializedSpannableNote";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_DATE_UPDATED = "dateUpdated";
    private static final DateFormat dt = new SimpleDateFormat("dd.MM.yyyy, hh:mm:ss", Locale.getDefault());

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_SPANNABLE_NOTE + " TEXT, "
                + KEY_IMAGE + " BLOB, "
                + KEY_DATE_UPDATED + " TEXT, "
                + KEY_NOTE_TITLE + " VARCHAR(100))" //We don't want a super long title...
                );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        switch (oldVersion)
        {
            case 1:
                //upgrade from version 1 to 2
                db.execSQL("ALTER TABLE " + TABLE_NOTES + " ADD COLUMN " + KEY_DATE_UPDATED + " TEXT;");
            case 2:
                //upgrade from version 2 to 3
                db.execSQL("ALTER TABLE " + TABLE_NOTES + " ADD COLUMN " + KEY_NOTE_TITLE + " VARCHAR(100);");
            case 3:
                //upgrade from version 3 to 4
                //db.execSQL();

                //and so on.. do not add breaks so that switch will
                //start at oldVersion, and run straight through to the latest

        }
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
        String spannableAsHtml = convertSpannableToHtmlString(note.getSpannable());
        String date = dt.format(new Date());

        ContentValues values = new ContentValues();

        values.put(KEY_SPANNABLE_NOTE, spannableAsHtml);
        values.put(KEY_NOTE_TITLE, note.getTitle());
        values.put(KEY_IMAGE, BitmapConverter.getBytes(note.getImage()));
        values.put(KEY_DATE_UPDATED, date);

        db.insert(TABLE_NOTES, null, values);
        db.close();
    }

    /**
     * Method used to get specified Note from Database
     * @param id KEY_ID of Note to get from Database
     * @return Note object with specified KEY_ID
     */
    public Note getNote(int id) throws SQLiteException {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTES, new String[]{KEY_ID, KEY_SPANNABLE_NOTE, KEY_IMAGE, KEY_DATE_UPDATED, KEY_NOTE_TITLE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (!cursor.moveToFirst()) {
            //Failed to load note. Maybe the user restored data from an incompatible backup?
            //Let's delete the problematic data
            deleteNote(id);
            throw new SQLiteException("Note doesn't exist");
        }

        String spannableAsHtml = cursor.getString(cursor.getColumnIndex(KEY_SPANNABLE_NOTE));
        Spannable spannable = convertHtmlStringToSpannable(spannableAsHtml);

        Bitmap image = BitmapConverter.getImage(cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE)));

        Date date;

        try {
            date = dt.parse(cursor.getString(cursor.getColumnIndex(KEY_DATE_UPDATED)));
        } catch (Exception e) {
            date = new Date();
            e.printStackTrace();
        }

        String title;
        try {
            title = cursor.getString(cursor.getColumnIndex(KEY_NOTE_TITLE));
        }catch (Exception e){
            title = "";
            e.printStackTrace();
        }

        db.close();
        cursor.close();
        return new Note(id, title, spannable, image, date);
    }

    /**
     * Method used to delete specified Note from Database
     * @param note Note to delete
     */
    public void deleteNote(Note note) {
        deleteNote(note.getId());
    }

    public void deleteNote(int noteId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NOTES, KEY_ID + "=?", new String[]{String.valueOf(noteId)});
        db.close();
    }

    /**
     * Method used to get count of notes in Database
     * @return count of notes in Database
     */
    public int getNoteCount() {
        SQLiteDatabase db = getReadableDatabase();
        int numberOfNotes = (int) DatabaseUtils.queryNumEntries(db, TABLE_NOTES);
        db.close();
        return numberOfNotes;
    }

    /**
     * Method used to update Note's text/format
     * @param note Note to update
     * @return updated Note
     */
    public int updateNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();

        String spannableAsHtml = convertSpannableToHtmlString(note.getSpannable());

        String date = dt.format(new Date());

        ContentValues values = new ContentValues();
        values.put(KEY_IMAGE, BitmapConverter.getBytes(note.getImage()));
        values.put(KEY_DATE_UPDATED, date);
        values.put(KEY_SPANNABLE_NOTE, spannableAsHtml);
        values.put(KEY_NOTE_TITLE, note.getTitle());

        return db.update(TABLE_NOTES, values, KEY_ID + "=?", new String[]{String.valueOf(note.getId())});
    }

    /**
     * Method used to get all notes in Database
     * @return ArrayList of Notes, containing all notes in Database
     */
    public ArrayList<Note> getAllNotesAsArrayList() {
        ArrayList<Note> notes = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES, null);

        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                Spannable spannable = convertHtmlStringToSpannable(cursor.getString(cursor.getColumnIndex(KEY_SPANNABLE_NOTE)));
                //Default val
                Date date;

                try {
                    date = dt.parse(cursor.getString(cursor.getColumnIndex(KEY_DATE_UPDATED)));
                } catch (Exception e) {
                    date = new Date();
                    e.printStackTrace();
                }

                String title;
                try {
                    title = cursor.getString(cursor.getColumnIndex(KEY_NOTE_TITLE));
                } catch (Exception e) {
                    title = "";
                    e.printStackTrace();
                }

                Note note = new Note(id, title, spannable, null, date);
                notes.add(note);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return notes;
    }

    private Spannable convertHtmlStringToSpannable(String htmlString) {
        Spanned spanned;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(htmlString, Html.FROM_HTML_MODE_LEGACY);
        } else {
            spanned = Html.fromHtml(htmlString);
        }

        final String newLine = "\n";
        // Html.toHtml() appends newLine to end of text.
        // This is workaround for that situation.
        while (spanned.toString().endsWith(newLine)) {
            spanned = new SpannedString(spanned.subSequence(0, spanned.length() - newLine.length()));
        }
        return new SpannableString(spanned);
    }

    private String convertSpannableToHtmlString(Spannable spannable) {
        String htmlString;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            htmlString = Html.toHtml(spannable, Html.FROM_HTML_MODE_LEGACY);
        } else {
            htmlString = Html.toHtml(spannable);
        }
        return removeAllSuffixingNewlines(htmlString);
    }

    private String removeAllSuffixingNewlines(String string) {
        // Html.toHtml() appends newLine to end of text.
        // This is workaround for that situation.
        final String newLine = "\n";
        while (string.lastIndexOf(newLine) != -1
                && string.lastIndexOf(newLine) == string.length() - newLine.length()) {
            string = string.substring(0, string.length() - newLine.length());
        }
        return string;
    }

}
