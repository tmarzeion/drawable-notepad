package com.example.tomek.notepad.epoxy.noteslist

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import com.airbnb.epoxy.EpoxyController
import com.bumptech.glide.Glide
import com.example.tomek.notepad.R
import com.example.tomek.notepad.database.DatabaseHandler
import com.example.tomek.notepad.model.Note
import org.apache.commons.lang3.StringUtils


class NotesListEpoxyController(var notes: MutableList<Note>, var selectedNotes: MutableSet<Int> = mutableSetOf(),
                               var deleteMode: Boolean = false, private val dbHandler: DatabaseHandler,
                               val context: Context, val onNoteActionPerformed: OnNoteActionPerformed,
                               val handler: Handler) : EpoxyController(handler, handler) {

    private var currentStringFilter: String = ""

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
                        .selected(selectedNotes.contains(it.id))
                        .onBind { model, view, position ->
                            handleCacheImageLoading(it, view)
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

    private val memoryCachedImages = mutableListOf<Pair<Int, Bitmap>>()
    private fun handleCacheImageLoading(it: Note, holder: Holder) {
        holder.setLoadingMode(true)
        handler.post {
            if (!memoryCachedImages.any { cachedImage -> cachedImage.first == it.id }) {
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

    fun filterByQuery(query: String) {
        currentStringFilter = query
    }

    fun revalidateCacheForNote(noteId: Int) {
        memoryCachedImages.removeAt(memoryCachedImages.indexOfFirst {
            it.first == noteId
        })
    }

    interface OnNoteActionPerformed {
        fun onNoteClicked(id: Int)
    }

    companion object {
        private const val MAX_CACHED_IMAGES = 100
    }

}