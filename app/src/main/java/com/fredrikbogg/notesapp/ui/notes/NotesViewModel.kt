package com.fredrikbogg.notesapp.ui.notes

import android.app.Application
import androidx.lifecycle.*
import com.fredrikbogg.notesapp.util.Event
import com.fredrikbogg.notesapp.data.db.FolderNoteRoomDatabase
import com.fredrikbogg.notesapp.data.db.entity.Note
import com.fredrikbogg.notesapp.data.db.repository.FolderNotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FolderNotesViewModelFactory(private val app: Application, private val folderId: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FolderNotesViewModel(app, folderId) as T
    }
}

class FolderNotesViewModel(app: Application, val folderId: String) : AndroidViewModel(app) {

    private val notesBeforeDeleting: MutableList<Note> = ArrayList()
    private val repository: FolderNotesRepository
    private val _notesDeletedEvent = MutableLiveData<Event<Unit>>()
    private val _folderDeletedEvent = MutableLiveData<Event<Unit>>()

    val notesDeletedEvent: LiveData<Event<Unit>> = _notesDeletedEvent
    val folderDeletedEvent: LiveData<Event<Unit>> = _folderDeletedEvent
    var selectedNote = MutableLiveData<Event<Note>>()
    val allNotesInFolder: LiveData<List<Note>>

    init {
        val folderNotesDao = FolderNoteRoomDatabase.getDatabase(app).folderNotesDao()
        repository = FolderNotesRepository(folderNotesDao, folderId)
        allNotesInFolder = repository.allNotesInFolder
    }

    fun noteSelected(note: Note) {
        selectedNote.value = Event(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteNote(note)
    }

    fun undoDeleteNotes() = viewModelScope.launch(Dispatchers.IO) {
        for (n in notesBeforeDeleting) {
            repository.insertNote(n)
        }
        notesBeforeDeleting.clear()
    }

    fun deleteAllNotes() = viewModelScope.launch {
        if (allNotesInFolder.value != null && allNotesInFolder.value!!.isNotEmpty()) {
            notesBeforeDeleting.addAll(allNotesInFolder.value as List)
            repository.deleteAllNotesFromFolder(folderId)
            _notesDeletedEvent.value = Event(Unit)
        }
    }

    fun deleteFolder() = viewModelScope.launch {
        repository.deleteFolder()
        repository.deleteAllNotesFromFolder(folderId)
        _folderDeletedEvent.value = Event(Unit)
    }

    fun changeFolderTitle(title: String) = viewModelScope.launch {
        repository.updateFolderTitle(title)
    }
}
