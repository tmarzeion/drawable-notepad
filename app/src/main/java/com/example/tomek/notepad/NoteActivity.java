package com.example.tomek.notepad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.tomek.notepad.FormatTextContainer;

public class NoteActivity extends AppCompatActivity {

    FormatTextContainer root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.root = (FormatTextContainer) this.getLayoutInflater().inflate(R.layout.activity_note, null);

        this.setContentView(root);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }
/*
    public void toggleMenu(View v){
        this.root.toggleMenu();
    }*/

    public void toggleMenu(MenuItem item) {
        this.root.toggleMenu();
    }
}
