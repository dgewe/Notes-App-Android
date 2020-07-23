package com.fredrikbogg.notesapp.ui.edit_note

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fredrikbogg.notesapp.util.Event
import com.fredrikbogg.notesapp.data.db.FolderNoteRoomDatabase
import com.fredrikbogg.notesapp.data.db.repository.NotesRepository
import com.fredrikbogg.notesapp.data.db.entity.Note
import kotlinx.coroutines.launch
import java.util.*

class EditNoteViewModel(app: Application) : AndroidViewModel(app) {

    private val repository: NotesRepository
    private val _noteUpdatedEvent = MutableLiveData<Event<Unit>>()
    private val _noteEmptyEvent = MutableLiveData<Event<Unit>>()
    private var isNewNote: Boolean = false
    val noteUpdatedEvent: LiveData<Event<Unit>> = _noteUpdatedEvent
    val noteEmptyEvent: LiveData<Event<Unit>> = _noteEmptyEvent
    val note = MutableLiveData<Note>()

    init {
        val notesDao = FolderNoteRoomDatabase.getDatabase(app).notesDao()
        repository = NotesRepository(notesDao)
    }

    private fun insertNote(newNote: Note) = viewModelScope.launch {
        repository.insertNote(newNote)
        _noteUpdatedEvent.value = Event(Unit)
    }

    private fun updateNote(updatedNote: Note) = viewModelScope.launch {
        repository.updateNote(updatedNote)
        _noteUpdatedEvent.value = Event(Unit)
    }

    private fun deleteNote(noteToDelete: Note) = viewModelScope.launch {
        repository.deleteNote(noteToDelete)
        _noteUpdatedEvent.value = Event(Unit)
    }

    fun start(note: Note?, startDescription: String?, folderId: String?) {
        val newNote = note ?: Note()

        if (note == null) {
            isNewNote = true
            startDescription?.let { newNote.description = it }
            folderId?.let { newNote.folderId = it }
        }

        this.note.value = newNote
    }

    fun saveNote() {
        val savedNote: Note = note.value!!

        if (savedNote.title.isEmpty() && savedNote.description.isEmpty()) {
            if (!isNewNote) {
                deleteNote(savedNote)
            } else {
                _noteUpdatedEvent.value =
                    Event(Unit)
            }
        } else {
            if (savedNote.title.isEmpty()) {
                _noteEmptyEvent.value = Event(Unit)
            } else {
                savedNote.dateMilliseconds = Date().time
                if (isNewNote) insertNote(savedNote) else updateNote(savedNote)
            }
        }
    }

    fun deleteNote() {
        note.value?.let {
            deleteNote(it)
        }
    }
}