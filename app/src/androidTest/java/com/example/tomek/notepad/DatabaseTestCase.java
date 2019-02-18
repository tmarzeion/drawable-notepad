package com.example.tomek.notepad;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.text.Spannable;
import android.text.SpannableString;

import com.example.tomek.notepad.database.DatabaseHandler;
import com.example.tomek.notepad.model.Note;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class DatabaseTestCase {

    @Before
    public void setup() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseHandler databaseHandler = new DatabaseHandler(appContext);
        databaseHandler.clearAllNotes();
    }

    @After
    public void cleanup() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseHandler databaseHandler = new DatabaseHandler(appContext);
        databaseHandler.clearAllNotes();
    }

    @Test
    public void shouldInsertNoteToDb() {
        Context appContext = InstrumentationRegistry.getTargetContext();

        // Database instance
        DatabaseHandler databaseHandler = new DatabaseHandler(appContext);

        // Properties of note to be added
        final String title = "Some random title :)";
        final Spannable text = new SpannableString("Some note text");
        final int bmp_width = 1000;
        final int bmp_height = 1000;
        final Bitmap bmp = Bitmap.createBitmap(bmp_width, bmp_height, Bitmap.Config.ARGB_8888);

        // Note that will be added
        Note noteToAdd = new Note();
        noteToAdd.setTitle(title);
        noteToAdd.setSpannable(text);
        noteToAdd.setImage(bmp);

        // Fetch notes from DB
        ArrayList<Note> notesInDb = databaseHandler.getAllNotesAsArrayList();

        // Check if DB is empty
        assertThat(notesInDb, is(empty()));

        // Add note to DB
        databaseHandler.createNote(noteToAdd);

        // Fetch notes from DB
        notesInDb = databaseHandler.getAllNotesAsArrayList();

        // Check if note was added
        assertThat(notesInDb, hasSize(1));

        // Get fetched note instance
        Note noteFromDb = notesInDb.get(0);

        databaseHandler.getNote(noteFromDb.getId());

        // Check if added note contains same properties
        assertThat(noteFromDb.getTitle(), is(title));
        assertThat(noteFromDb.getSpannable(), is(text));
        assertTrue(noteFromDb.getImage().sameAs(bmp));

    }

    @Test
    public void shouldInsertManyNotesToDb() {

        final int notesCount = 1000;

        Context appContext = InstrumentationRegistry.getTargetContext();

        // Database instance
        DatabaseHandler databaseHandler = new DatabaseHandler(appContext);

        // Properties of note to be added
        final String title = "Some random title :)";
        final Spannable text = new SpannableString("Some note text");
        final int bmp_width = 1000;
        final int bmp_height = 1000;
        final Bitmap bmp = Bitmap.createBitmap(bmp_width, bmp_height, Bitmap.Config.ARGB_8888);

        // Note that will be added
        Note noteToAdd = new Note();
        noteToAdd.setTitle(title);
        noteToAdd.setSpannable(text);
        noteToAdd.setImage(bmp);

        // Fetch notes from DB
        ArrayList<Note> notesInDb = databaseHandler.getAllNotesAsArrayList();

        // Check if DB is empty
        assertThat(notesInDb, is(empty()));

        // Add notes to DB
        for (int i = 0; i < notesCount; i++) {
            databaseHandler.createNote(noteToAdd);
            if (i%10 == 0) System.out.println("TEST NOTE: " + i);
        }

        // Fetch notes from DB
        notesInDb = databaseHandler.getAllNotesAsArrayList();

        // Check if note was added
        assertThat(notesInDb, hasSize(1000));

        // Get fetched note instance
        Note noteFromDb = notesInDb.get(0);

        databaseHandler.getNote(noteFromDb.getId());

        // Check if added note contains same properties
        assertThat(noteFromDb.getTitle(), is(title));
        assertThat(noteFromDb.getSpannable(), is(text));
        assertTrue(noteFromDb.getImage().sameAs(bmp));

    }

}