package com.example.tomek.notepad.epoxy.noteslist

import android.graphics.Bitmap
import android.text.Spannable
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
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

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var listener: View.OnClickListener? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var longClickListener: View.OnLongClickListener? = null

    override fun bind(holder: Holder) {
        holder.titleView.text = title
        holder.noteContent.text = content
        holder.numberView.text = "#${noteId}"
        holder.numberView.tag = noteId
        holder.noteClickableOverlay.setOnClickListener(listener)
        holder.noteClickableOverlay.setOnLongClickListener(longClickListener)
        holder.checkbox.setOnClickListener(listener)
    }

}

class Holder : KotlinHolder() {
    val titleView by bind<TextView>(R.id.noteTitle)
    val numberView by bind<TextView>(R.id.noteNumber)
    val noteContent by bind<TextView>(R.id.noteContent)
    val noteImage by bind<ImageView>(R.id.noteImage)
    val noteClickableOverlay by bind<View>(R.id.noteClickableOverlay)
    val checkbox by bind<CheckBox>(R.id.checkbox)

    fun setLoadingMode(loading: Boolean) {
        if (loading) {
            noteImage.alpha = 0.0f
        } else {
            noteImage.animate().alpha(0.5f)
        }
    }

}