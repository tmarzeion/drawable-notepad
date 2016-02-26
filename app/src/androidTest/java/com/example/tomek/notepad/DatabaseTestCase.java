package com.example.tomek.notepad;

import android.graphics.Bitmap;
import android.test.AndroidTestCase;
import android.text.SpannableString;

/**
 * Created by tomek on 22.02.16.
 */
public class DatabaseTestCase extends AndroidTestCase {

    private final int FIRST_NOTE_ID = 1;
    private final int SECOND_NOTE_ID = 3;
    private final int THIRD_NOTE_ID = 4;
    private final String FIRST_NOTE_TEXT = "<b>first</b>";
    private final String SECOND_NOTE_TEXT = " second another second ";
    private final String THIRD_NOTE_TEXT = "third </i>";
    private Bitmap firstBitmap;
    private Bitmap secondBitmap;
    private Bitmap thirdBitmap;




    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        firstBitmap = Bitmap.createBitmap(1000, 500, Bitmap.Config.ARGB_8888);
        secondBitmap = Bitmap.createBitmap(2000, 50, Bitmap.Config.ARGB_8888);
        thirdBitmap = Bitmap.createBitmap(500, 1, Bitmap.Config.ARGB_8888);
    }

    public void testNoteDatabase() {
        DatabaseHandler databaseHandler = new DatabaseHandler(getContext());

        Note firstNote = new Note(FIRST_NOTE_ID, new SpannableString(FIRST_NOTE_TEXT), firstBitmap);
        Note secondNote = new Note(SECOND_NOTE_ID, new SpannableString(SECOND_NOTE_TEXT), secondBitmap);
        Note thirdNote = new Note(THIRD_NOTE_ID, new SpannableString(THIRD_NOTE_TEXT), thirdBitmap);

        databaseHandler.createNote(firstNote);
        databaseHandler.createNote(secondNote);
        databaseHandler.createNote(thirdNote);

        Note firstRecievedNote = databaseHandler.getNote(FIRST_NOTE_ID);
        Note secondRecievedNote = databaseHandler.getNote(SECOND_NOTE_ID);
    //    Note thirdRecievedNote = databaseHandler.getNote(THIRD_NOTE_ID);

        assertNotNull(firstRecievedNote);
        assertEquals(FIRST_NOTE_ID, firstRecievedNote.getId());
        assertEquals(new SpannableString(FIRST_NOTE_TEXT), firstRecievedNote.getSpannable());
//        assertEquals("first",firstRecievedNote.getRawText());
        assertEquals(firstBitmap.getHeight(), firstRecievedNote.getImage().getHeight());
        assertEquals(firstBitmap.getWidth(), firstRecievedNote.getImage().getWidth());
        databaseHandler.deleteNote(firstRecievedNote);
//        assertNull(databaseHandler.getNote(FIRST_NOTE_ID));

        assertNotNull(secondRecievedNote);
        assertEquals(SECOND_NOTE_ID, secondRecievedNote.getId());
        assertEquals(new SpannableString(SECOND_NOTE_TEXT), secondRecievedNote.getSpannable());
        assertEquals(SECOND_NOTE_TEXT,secondRecievedNote.getRawText());
        assertEquals(secondBitmap.getHeight(), secondRecievedNote.getImage().getHeight());
        assertEquals(secondBitmap.getWidth(), secondRecievedNote.getImage().getWidth());
        databaseHandler.deleteNote(secondRecievedNote);
 //       assertNull(databaseHandler.getNote(SECOND_NOTE_ID));
/*
        assertNotNull(thirdRecievedNote);
        assertEquals(THIRD_NOTE_ID, thirdRecievedNote.getId());
        assertEquals(new SpannableString(THIRD_NOTE_TEXT), thirdRecievedNote.getSpannable());
        assertEquals(THIRD_NOTE_TEXT,thirdRecievedNote.getRawText());
        assertEquals(thirdBitmap.getHeight(), thirdRecievedNote.getImage().getHeight());
        assertEquals(thirdBitmap.getWidth(), thirdRecievedNote.getImage().getWidth());
        databaseHandler.deleteNote(thirdRecievedNote);
        assertNull(databaseHandler.getNote(THIRD_NOTE_ID));
        */
    }
}
