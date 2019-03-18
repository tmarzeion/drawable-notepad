package com.example.tomek.notepad.epoxy.noteslist

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.view.View
import com.airbnb.epoxy.EpoxyController
import com.bumptech.glide.Glide
import com.example.tomek.notepad.R
import com.example.tomek.notepad.database.DatabaseHandler
import com.example.tomek.notepad.model.Note
import org.apache.commons.lang3.StringUtils
import java.util.*


class NotesListEpoxyController(var notes: MutableList<Note>, var selectedNotes: MutableSet<Int> = mutableSetOf(),
                               private val dbHandler: DatabaseHandler, val context: Context, val onNoteActionPerformed: OnNoteActionPerformed,
                               val handler: Handler) : EpoxyController(handler, handler) {

    private var deleteMode: Boolean = false
    private var currentStringFilter: String = ""
    private val timerMap = mutableMapOf<Holder, Timer>()
    private val holders: MutableSet<Holder> = mutableSetOf()
    private val memoryCachedImages = mutableListOf<Pair<Int, Bitmap>>()

    override fun buildModels() {
        if (notes.isEmpty()) {
            //TODO
        } else {
            notes.filter {
                it.title.contains(currentStringFilter)
                        || it.rawText.contains(currentStringFilter)
            }.forEach {
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
                        .deleteMode(deleteMode)
                        .onBind { model, holder, position ->
                            holders.add(holder)
                            holder.noteImage.setImageBitmap(null)
                            holder.checkbox.isChecked = selectedNotes.contains(it.id)
                            if (deleteMode) {
                                holder.checkbox.visibility = View.VISIBLE
                                holder.checkbox.alpha = 1.0f
                            } else {
                                holder.checkbox.visibility = View.GONE
                                holder.checkbox.alpha = 0.0f
                            }
                            holder.setLoadingMode(true)
                            val timer = Timer()
                            timerMap[holder] = timer
                            timer.schedule(object : TimerTask() {
                                override fun run() {
                                    handleCacheImageLoading(it, holder)
                                }
                            }, 600L)
                        }
                        .onUnbind { model, holder ->
                            timerMap[holder]?.cancel()
                        }
                        .listener { model, parentView, clickedView, position ->
                            when(clickedView.id) {
                                R.id.checkbox -> {
                                    if (selectedNotes.contains(it.id)) {
                                        selectedNotes.remove(it.id)
                                    } else {
                                        selectedNotes.add(it.id)
                                    }
                                    requestModelBuild()
                                }
                                R.id.noteClickableOverlay -> onNoteActionPerformed.onNoteClicked(it.id)
                            }
                        }
                        .addTo(this)
            }
        }
    }

    private fun handleCacheImageLoading(it: Note, holder: Holder) {
        handler.post {
            if (!hasCachedImage(it)) {
                if (memoryCachedImages.size >= MAX_CACHED_IMAGES) {
                    memoryCachedImages.remove(memoryCachedImages.first())
                }
                memoryCachedImages.add(Pair(it.id, dbHandler.getNote(it.id).image))
            }
            val resolvedImage = memoryCachedImages[memoryCachedImages.indexOfFirst {
                cachedImage -> cachedImage.first == it.id }
            ].second
            Handler(Looper.getMainLooper()).post {
                Glide.with(holder.noteImage.context)
                        .load(resolvedImage)
                        .into(holder.noteImage)
                holder.setLoadingMode(false)
            }
        }
    }

    private fun hasCachedImage(note: Note) : Boolean {
        return memoryCachedImages.any { cachedImage -> cachedImage.first == note.id }
    }

    fun filterByQuery(query: String) {
        currentStringFilter = query
    }

    fun setDeleteMode(deleteModeEnabled: Boolean) {
        deleteMode = deleteModeEnabled
        holders.forEach {
            it.checkbox.isChecked = false
            if (deleteMode) {
                it.checkbox.visibility = View.VISIBLE
                it.checkbox.animate().alpha(1.0f).duration = CHECKBOX_FADE_TIME
            } else {
                Handler(Looper.getMainLooper()).postDelayed({ it.checkbox.visibility = View.GONE }, CHECKBOX_FADE_TIME)
                it.checkbox.animate().alpha(0.0f).duration = CHECKBOX_FADE_TIME
            }
        }
    }

    fun invalidateBitmap(noteId: Int, bitmap: Bitmap) {
        memoryCachedImages.removeAll {
            it.first == noteId
        }
        memoryCachedImages.add(Pair(noteId, bitmap))
        holders.firstOrNull {
            it.numberView.tag == noteId
        }?.noteImage?.setImageBitmap(bitmap)
    }

    fun isDeleteMode() : Boolean {
        return deleteMode
    }

    interface OnNoteActionPerformed {
        fun onNoteClicked(id: Int)
    }

    companion object {
        private const val CHECKBOX_FADE_TIME = 300L
        private const val MAX_CACHED_IMAGES = 100
    }

}