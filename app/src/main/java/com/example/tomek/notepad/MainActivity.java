package com.example.tomek.notepad;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create DatabaseHandler
        dbHandler = new DatabaseHandler(getApplicationContext());

        // Add items to ListView
        populateListView(dbHandler.getAllNotes());

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

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        Runtime.getRuntime().gc();
    }

    /**
     * Method used to fill ListView
     * @param allNotes Array of Notes containing all Notes in Database
     */
    private void populateListView(ArrayList<Note> allNotes) {

        String[] noteTitles = new String[allNotes.size()];

        for (int i = 0; i < allNotes.size(); i++) {
            noteTitles[i] = allNotes.get(i).getTitle();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.list_view_item_main, noteTitles);

        ListView list = (ListView) findViewById(R.id.listView);
        list.setAdapter(arrayAdapter);
    }
}
