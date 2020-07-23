package com.fredrikbogg.notesapp.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fredrikbogg.notesapp.data.db.entity.Folder

@Dao
interface FoldersDao {
    @Query("SELECT * from folder_table")
    fun getAllFolders(): LiveData<List<Folder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolder(folder: Folder)

    @Query("DELETE FROM folder_table")
    suspend fun deleteAllFolders()

    @Query("DELETE FROM folder_table WHERE id = :folderId ")
    suspend fun deleteFolder(folderId: String)

    @Query("UPDATE folder_table SET title = :title WHERE id = :folderId")
    suspend fun updateFolderTitle(title: String, folderId: String)
}