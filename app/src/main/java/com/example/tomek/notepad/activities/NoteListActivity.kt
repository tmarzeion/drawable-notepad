package com.example.tomek.notepad.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Menu
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import com.example.tomek.notepad.database.DatabaseHandler
import com.example.tomek.notepad.R
import com.example.tomek.notepad.epoxy.noteslist.NotesListEpoxyController
import kotlinx.android.synthetic.main.activity_note_list.*

/**
 * Main Activity class
 */
class NoteListActivity : BaseActivity(), NotesListEpoxyController.OnNoteActionPerformed {

    // Database Handler
    private lateinit var dbHandler: DatabaseHandler

    // Epoxy Controller
    private lateinit var epoxyController: NotesListEpoxyController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        // Create DatabaseHandler
        dbHandler = DatabaseHandler(applicationContext)

        // Floating Action Button listener used to adding new notes
        fab.setOnClickListener {
            hideSoftKeyboard()
            val intent = Intent(this, NoteActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("id", "-1")
            startActivity(intent)
        }

        // Thread for handling background model diffing/building
        val handlerThread = HandlerThread("epoxy")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        epoxyController = NotesListEpoxyController(dbHandler.allNotesAsArrayList, dbHandler, this, this, handler)
        notesRecyclerView.adapter = epoxyController.adapter
        notesRecyclerView.layoutManager = LinearLayoutManager(this)

    }

    override fun onResume() {
        super.onResume()
        epoxyController.notes = dbHandler.allNotesAsArrayList
        epoxyController.requestModelBuild()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Creating menu
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.queryHint = searchView.context.resources.getString(R.string.search_hint)

        searchView.setOnCloseListener {
            hideSoftKeyboard()
            false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                epoxyController.filterByQuery(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.onActionViewCollapsed()
                return true
            }
        })

        return true
    }

    override fun onNoteClicked(id: Int) {
        editNote(id)
    }

    /**
     * Method used to enter note edition mode
     *
     * @param noteId ID number of the Note entry in the SQLite database
     */
    private fun editNote(noteId: Int) {
        hideSoftKeyboard()
        val intent = Intent(this@NoteListActivity, NoteActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("id", noteId.toString())
        startActivity(intent)
    }

}
