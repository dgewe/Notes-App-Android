package com.fredrikbogg.notesapp.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.fredrikbogg.notesapp.data.db.entity.Note

@Dao
interface NotesDao {
    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE FROM note_table WHERE folderId = :folderId")
    suspend fun deleteAllNotesFromFolder(folderId: String)

    @Query("DELETE FROM note_table")
    suspend fun deleteAllNotes()

    @Query("SELECT * FROM note_table WHERE folderId = :folderId ORDER BY date DESC")
    fun getAllNotesFromFolder(folderId: String): LiveData<List<Note>>

    @Update
    suspend fun updateNote(note: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)
}