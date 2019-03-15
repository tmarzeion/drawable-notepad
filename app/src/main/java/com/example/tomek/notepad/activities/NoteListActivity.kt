package com.example.tomek.notepad.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import com.example.tomek.notepad.database.DatabaseHandler
import com.example.tomek.notepad.R
import com.example.tomek.notepad.epoxy.noteslist.NotesListEpoxyController
import com.example.tomek.notepad.model.Note
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
            if (epoxyController.deleteMode) {
                epoxyController.selectedNotes.forEach {
                    dbHandler.deleteNote(it)
                }
                epoxyController.notes = dbHandler.allNotesAsArrayList
                epoxyController.selectedNotes.clear()
                epoxyController.requestModelBuild()
            } else {
                hideSoftKeyboard()
                val intent = Intent(this, NoteActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("id", "-1")
                startActivity(intent)
            }
        }

        // Thread for handling background model diffing/building
        val handlerThread = HandlerThread("epoxy")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        epoxyController = NotesListEpoxyController(mutableListOf(), mutableSetOf(), false, dbHandler, this, this, handler)

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
                epoxyController.requestModelBuild()
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.onActionViewCollapsed()
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_delete_all_notes -> toggleDeleteMode(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toggleDeleteMode(item: MenuItem) {
        epoxyController.selectedNotes.clear()
        epoxyController.deleteMode = !epoxyController.deleteMode
        if (epoxyController.deleteMode) {
            item.setIcon(R.drawable.ic_close)
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check))
        } else {
            item.setIcon(R.drawable.ic_delete_24px)
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_add))
        }
        epoxyController.requestModelBuild()
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
        epoxyController.revalidateCacheForNote(noteId)
        val intent = Intent(this@NoteListActivity, NoteActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("id", noteId.toString())
        startActivity(intent)
    }

}
