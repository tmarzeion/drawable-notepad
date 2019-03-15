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

    @EpoxyAttribute
    var deleteMode: Boolean = false

    @EpoxyAttribute
    var selected: Boolean = false

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var listener: View.OnClickListener? = null

    override fun bind(holder: Holder) {
        holder.titleView.text = title
        holder.noteContent.text = content
        holder.numberView.text = "#${noteId}"
        holder.noteClickableOverlay.setOnClickListener(listener)
        holder.checkbox.setOnClickListener(listener)
        holder.checkbox.isChecked = selected

        if (deleteMode) {
            holder.checkbox.visibility = View.VISIBLE
        } else {
            holder.checkbox.visibility = View.GONE
        }
    }

}

class Holder : KotlinHolder() {
    val titleView by bind<TextView>(R.id.noteTitle)
    val numberView by bind<TextView>(R.id.noteNumber)
    val noteContent by bind<TextView>(R.id.noteContent)
    val noteImage by bind<ImageView>(R.id.noteImage)
    val noteClickableOverlay by bind<View>(R.id.noteClickableOverlay)
    val checkbox by bind<CheckBox>(R.id.checkbox)
    val loadingWrapper by bind<ViewGroup>(R.id.loadingWrapper)
    val contentWrapper by bind<ViewGroup>(R.id.contentWrapper)

    fun setLoadingMode(loading: Boolean) {
        if (loading) {
            loadingWrapper.visibility = View.VISIBLE
            contentWrapper.visibility = View.INVISIBLE
        } else {
            loadingWrapper.visibility = View.INVISIBLE
            contentWrapper.visibility = View.VISIBLE
        }
    }

}