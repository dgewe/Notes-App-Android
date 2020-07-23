package com.fredrikbogg.notesapp.data.db.repository

import androidx.lifecycle.LiveData

import com.fredrikbogg.notesapp.data.db.entity.Folder
import com.fredrikbogg.notesapp.data.db.dao.FoldersDao

class FoldersRepository(private val foldersDao: FoldersDao) {
    val allFolders: LiveData<List<Folder>> = foldersDao.getAllFolders()

    suspend fun insertFolder(folder: Folder) {
        foldersDao.insertFolder(folder)
    }
}