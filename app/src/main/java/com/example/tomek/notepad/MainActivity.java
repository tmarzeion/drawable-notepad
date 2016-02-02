package com.example.tomek.notepad;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * Main Activity class
 */
public class MainActivity extends AppCompatActivity {

    // Database Handler
    private DatabaseHandler dbHandler;

    // Alert dialogs for back button and delete all notes button
    private AlertDialog alertDialogCloseApp;
    private AlertDialog alertDialogDeleteAll;

    // Note selected on menu
    private Note selectedNote;

    // Variables used to handle note list
    private NoteAdapter noteAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create DatabaseHandler
        dbHandler = new DatabaseHandler(getApplicationContext());

        // Add items to ListView
        listView = (ListView) findViewById(R.id.listView);
        populateListView(dbHandler.getAllNotesAsArray());

        // Assign listView to context menu
        registerForContextMenu(listView);

        // Setup AlertDialogs
        alertDialogDeleteAll = initAlertDialogDeleteAllNotes();
        alertDialogCloseApp = initAlertDialogCloseApp();

        // Floating Action Button listener used to adding new notes
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("id", "-1");
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Creating menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Method used for first setup of back button AlertDialog
     */
    private AlertDialog initAlertDialogCloseApp() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setTitle("Close Application");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (android.os.Build.VERSION.SDK_INT >= 16) {
                    MainActivity.this.finishAffinity();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder.create();
    }

    /**
     * Method used for first setup of delete all notes button AlertDialog
     */
    private AlertDialog initAlertDialogDeleteAllNotes() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setTitle("Delete all notes");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAllNotes();
                Toast.makeText(MainActivity.this, "All notes has been deleted!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

    /**
     * Method used to show AlertDialog when delete all notes button is clicked
     */
    public void showAlertDialog(MenuItem menuItem) {
        alertDialogDeleteAll.show();
    }

    /**
     * Method that Overrides back button behavior
     * When back button is pressed it shows "back button" AlertDialog
     */
    @Override
    public void onBackPressed() {
        alertDialogCloseApp.show();
    }

    /**
     * Method used to delete all notes via DatabaseHandler
     */
    public void deleteAllNotes() {
        dbHandler.clearAllNotes();
        populateListView(dbHandler.getAllNotesAsArray());
        noteAdapter.notifyDataSetChanged();
    }

    /**
     * Method used to fill ListView
     * @param allNotes Array of Notes containing all Notes in Database
     */
    private void populateListView(Note[] allNotes) {
        noteAdapter = new NoteAdapter(this,
                R.layout.listview_item_row, allNotes);
        listView.setAdapter(noteAdapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.listView) {
            ListView listView = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            selectedNote = (Note) listView.getItemAtPosition(acmi.position);
            menu.setHeaderTitle("Choose action for note #" + selectedNote.getId());
            MenuInflater inflater =getMenuInflater();
            inflater.inflate(R.menu.context_menu_note_select, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.context_menu_delete:
                dbHandler.deleteNote(selectedNote);
                populateListView(dbHandler.getAllNotesAsArray());
                noteAdapter.notifyDataSetChanged();
                break;
            case R.id.context_menu_edit:

                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("id", String.valueOf(selectedNote.getId()));
                startActivity(intent);
                break;
        }
        return super.onContextItemSelected(item);
    }
}
