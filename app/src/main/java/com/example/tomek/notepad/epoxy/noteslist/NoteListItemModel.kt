package com.example.tomek.notepad.epoxy.noteslist

import android.graphics.Bitmap
import android.text.Spannable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.tomek.notepad.R
import com.example.tomek.notepad.epoxy.KotlinHolder

@EpoxyModelClass(layout = R.layout.item_note)
abstract class NoteListItemModel : EpoxyModelWithHolder<Holder>() {

    @EpoxyAttribute
    lateinit var title: String

    @EpoxyAttribute
    var noteId: Int = 0

    @EpoxyAttribute
    lateinit var content: Spannable

    @EpoxyAttribute
    var bitmap: Bitmap? = null

    @EpoxyAttribute
    var listener: View.OnClickListener? = null

    override fun bind(holder: Holder) {
        holder.titleView.text = title
        holder.noteContent.text = content
        holder.noteImage.setImageBitmap(bitmap)
        holder.numberView.text = "#${noteId}"
        holder.noteClickableOverlay.setOnClickListener(listener)
    }

}

class Holder : KotlinHolder() {
    val titleView by bind<TextView>(R.id.noteTitle)
    val numberView by bind<TextView>(R.id.noteNumber)
    val noteContent by bind<TextView>(R.id.noteContent)
    val noteImage by bind<ImageView>(R.id.noteImage)
    val noteClickableOverlay by bind<View>(R.id.noteClickableOverlay)
}