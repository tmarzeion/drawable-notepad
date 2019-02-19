package com.example.tomek.notepad.epoxy.noteslist

import android.content.Context
import android.os.Handler
import com.airbnb.epoxy.EpoxyController
import com.example.tomek.notepad.R
import com.example.tomek.notepad.database.DatabaseHandler
import com.example.tomek.notepad.model.Note
import org.apache.commons.lang3.StringUtils

class NotesListEpoxyController(var notes: MutableList<Note>, private val dbHandler: DatabaseHandler,
                               val context: Context, val onNoteActionPerformed: OnNoteActionPerformed,
                               handler: Handler) : EpoxyController(handler, handler) {

    override fun buildModels() {
        if (notes.isEmpty()) {
            //TODO
        } else {
            notes.forEach {
                val title =
                if (StringUtils.isNotEmpty(it.title)) {
                    it.title
                } else {
                    context.getString(R.string.no_title)
                }

                NoteListItemModel_()
                        .id(it.id)
                        .noteId(it.id)
                        .title(title)
                        .content(it.spannable)
                        .listener { _ ->
                            onNoteActionPerformed.onNoteClicked(it.id)
                        }
                        .bitmap(dbHandler.getNote(it.id).image)
                        .addTo(this)
            }
        }
    }

    interface OnNoteActionPerformed {
        fun onNoteClicked(id: Int)
    }

}