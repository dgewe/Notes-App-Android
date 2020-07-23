package com.fredrikbogg.notesapp.data.db.repository

import com.fredrikbogg.notesapp.data.db.dao.NotesDao
import com.fredrikbogg.notesapp.data.db.entity.Note

class NotesRepository(private val notesDao: NotesDao) {
    suspend fun updateNote(note: Note) {
        notesDao.updateNote(note)
    }

    suspend fun insertNote(note: Note) {
        notesDao.insertNote(note)
    }

    suspend fun deleteNote(note: Note) {
        notesDao.deleteNote(note)
    }
}