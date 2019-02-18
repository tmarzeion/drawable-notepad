package com.example.tomek.notepad.epoxy.noteslist

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.example.tomek.notepad.R
import com.example.tomek.notepad.database.DatabaseHandler
import com.example.tomek.notepad.model.Note
import org.apache.commons.lang3.StringUtils

class NotesListEpoxyController(var notes: MutableList<Note>, val dbHandler: DatabaseHandler, val context: Context) : EpoxyController() {

    override fun buildModels() {
        if (notes.isEmpty()) {

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
                        .bitmap(dbHandler.getNote(it.id).image)
                        .addTo(this)
            }
        }
    }

}