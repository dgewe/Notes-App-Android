package com.fredrikbogg.notesapp.ui.main

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fredrikbogg.notesapp.data.db.entity.Folder


@BindingAdapter("folder_items")
fun bindSetFolderItems(listView: RecyclerView, items: List<Folder>?) {
    items?.let { (listView.adapter as MainFoldersAdapter).submitList(items) }
}
