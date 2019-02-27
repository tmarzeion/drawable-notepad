package com.example.tomek.notepad.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, NoteListActivity::class.java)
        startActivity(intent)
        finish()
    }
}