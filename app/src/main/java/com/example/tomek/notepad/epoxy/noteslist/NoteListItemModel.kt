package com.example.tomek.notepad.epoxy.noteslist

import android.graphics.Bitmap
import android.text.Spannable
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.tomek.notepad.R
import com.example.tomek.notepad.epoxy.KotlinHolder

@EpoxyModelClass(layout = R.layout.note_item)
abstract class NoteListItemModel : EpoxyModelWithHolder<Holder>() {

    @EpoxyAttribute
    lateinit var title: String

    @EpoxyAttribute
    var noteId: Int = 0

    @EpoxyAttribute
    lateinit var content: Spannable

    @EpoxyAttribute
    var bitmap: Bitmap? = null

    override fun bind(holder: Holder) {
        holder.titleView.text = title
        holder.noteContent.text = content
        holder.noteImage.setImageBitmap(bitmap)
        holder.numberView.text = "#${noteId}"
    }

}

class Holder : KotlinHolder() {
    val titleView by bind<TextView>(R.id.noteTitle)
    val numberView by bind<TextView>(R.id.noteNumber)
    val noteContent by bind<TextView>(R.id.noteContent)
    val noteImage by bind<ImageView>(R.id.noteImage)
}