package com.example.tomek.notepad;

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
import android.widget.SimpleAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHandler = new DatabaseHandler(getApplicationContext());
        populateListView(dbHandler.getAllNotes());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.putExtra("id", "-1");
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete_note) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //TODO Confirm popup menu
    //TODO Refresh db after deleting
    //TODO New Activity when back button is clicked
    public void deleteAllNotes(MenuItem item) {
        dbHandler.clearAllNotes();
    }

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
