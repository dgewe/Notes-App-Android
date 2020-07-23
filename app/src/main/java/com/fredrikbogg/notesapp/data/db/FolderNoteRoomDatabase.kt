package com.fredrikbogg.notesapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fredrikbogg.notesapp.data.db.dao.FolderNotesDao
import com.fredrikbogg.notesapp.data.db.dao.FoldersDao
import com.fredrikbogg.notesapp.data.db.dao.NotesDao

import com.fredrikbogg.notesapp.data.db.entity.Folder
import com.fredrikbogg.notesapp.data.db.entity.Note

@Database(entities = [Note::class, Folder::class], version = 1, exportSchema = false)
abstract class FolderNoteRoomDatabase : RoomDatabase() {

    abstract fun notesDao(): NotesDao
    abstract fun foldersDao(): FoldersDao
    abstract fun folderNotesDao(): FolderNotesDao

    private class NoteDatabaseCallback : RoomDatabase.Callback()

    companion object {
        @Volatile
        private var INSTANCE: FolderNoteRoomDatabase? = null

        fun getDatabase(context: Context): FolderNoteRoomDatabase {
            return INSTANCE
                ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        FolderNoteRoomDatabase::class.java,
                        "note_database"
                    ).addCallback(NoteDatabaseCallback()).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                    instance
                }
        }
    }
}