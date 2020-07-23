package com.fredrikbogg.notesapp.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fredrikbogg.notesapp.util.Event
import com.fredrikbogg.notesapp.data.db.FolderNoteRoomDatabase
import com.fredrikbogg.notesapp.data.db.repository.FoldersRepository
import com.fredrikbogg.notesapp.data.db.entity.Folder
import kotlinx.coroutines.launch

class MainFoldersViewModel(app: Application) : AndroidViewModel(app) {
    private val repository: FoldersRepository
    val allFolders: LiveData<List<Folder>>

    var selectedFolderId = MutableLiveData<Event<String>>()

    init {
        val folderNotesDao = FolderNoteRoomDatabase.getDatabase(app).foldersDao()
        repository = FoldersRepository(folderNotesDao)
        allFolders = repository.allFolders
    }

    fun defaultFolderSelected() {
        selectedFolderId.value = Event("")
    }

    fun folderSelected(folder: Folder) {
        selectedFolderId.value = Event(folder.id)
    }

    fun insertFolder() = viewModelScope.launch {
        repository.insertFolder(Folder())
    }
}


