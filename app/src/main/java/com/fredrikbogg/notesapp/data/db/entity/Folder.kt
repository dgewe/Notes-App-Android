package com.fredrikbogg.notesapp.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "folder_table")
data class Folder(
    @ColumnInfo(name = "title") var title: String = "New folder",
    @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString()
)