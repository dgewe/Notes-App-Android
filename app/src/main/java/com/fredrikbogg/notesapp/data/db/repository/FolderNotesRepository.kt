package com.fredrikbogg.notesapp.data.db.repository

import androidx.lifecycle.LiveData
import com.fredrikbogg.notesapp.data.db.dao.FolderNotesDao
import com.fredrikbogg.notesapp.data.db.entity.Note

class FolderNotesRepository(
    private val folderNotesDao: FolderNotesDao,
    private val folderId: String
) {
    val allNotesInFolder: LiveData<List<Note>> = folderNotesDao.getAllNotesFromFolder(folderId)

    suspend fun deleteNote(note: Note) {
        folderNotesDao.deleteNote(note)
    }

    suspend fun insertNote(note: Note) {
        folderNotesDao.insertNote(note)
    }

    suspend fun deleteAllNotesFromFolder(folderId: String) {
        folderNotesDao.deleteAllNotesFromFolder(folderId)
    }

    suspend fun updateFolderTitle(title: String) {
        folderNotesDao.updateFolderTitle(title, folderId)
    }

    suspend fun deleteFolder() {
        folderNotesDao.deleteFolder(folderId)
    }
}