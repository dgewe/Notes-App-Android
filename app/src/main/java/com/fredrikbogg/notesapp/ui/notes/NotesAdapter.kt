package com.fredrikbogg.notesapp.ui.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fredrikbogg.notesapp.databinding.ListItemNoteBinding
import com.fredrikbogg.notesapp.data.db.entity.Note


class NoteListAdapter internal constructor(private val viewModel: FolderNotesViewModel) :
    ListAdapter<Note, NoteListAdapter.ViewHolder>(NoteDiffCallback()) {

    class ViewHolder(private val binding: ListItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModelFolder: FolderNotesViewModel, item: Note) {
            binding.viewmodel = viewModelFolder
            binding.note = item
            binding.executePendingBindings()
        }
    }

    fun getNoteAtPosition(position: Int): Note {
        return getItem(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemNoteBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }
}

class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }
}
