package com.fredrikbogg.notesapp.ui.notes

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.fredrikbogg.notesapp.data.db.entity.Note
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("note_items")
fun bindSetNoteItems(listView: RecyclerView, items: List<Note>?) {
    items?.let { (listView.adapter as NoteListAdapter).submitList(items) }
}

@BindingAdapter("swipe_item")
fun bindRecyclerViewSwipeItem(listView: RecyclerView, viewModel: FolderNotesViewModel) {
    val itemTouchHelperCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val note =
                    (listView.adapter as NoteListAdapter).getNoteAtPosition(viewHolder.adapterPosition)
                viewModel.deleteNote(note)
            }
        }

    ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(listView)
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("millisecondsToDate")
fun TextView.bindMillisecondsToDate(dateInMilliseconds: Long) {
    val pat = SimpleDateFormat().toLocalizedPattern().replace("\\W?[Yy]+\\W?".toRegex(), " ")
    val formatter = SimpleDateFormat(pat, Locale.getDefault())
    this.text = formatter.format(Date(dateInMilliseconds))
}

