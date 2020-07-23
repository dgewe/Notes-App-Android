package com.fredrikbogg.notesapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fredrikbogg.notesapp.databinding.ListItemFolderBinding
import com.fredrikbogg.notesapp.data.db.entity.Folder


class MainFoldersAdapter internal constructor(private val viewModel: MainFoldersViewModel) :
    ListAdapter<Folder, MainFoldersAdapter.ViewHolder>(FolderDiffCallback()) {

    class ViewHolder(private val binding: ListItemFolderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: MainFoldersViewModel, item: Folder) {
            binding.viewmodel = viewModel
            binding.folder = item
            binding.executePendingBindings()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemFolderBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }
}

class FolderDiffCallback : DiffUtil.ItemCallback<Folder>() {
    override fun areItemsTheSame(oldItem: Folder, newItem: Folder): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Folder, newItem: Folder): Boolean {
        return oldItem == newItem
    }
}
