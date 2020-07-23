package com.fredrikbogg.notesapp.data.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*


@Entity(tableName = "note_table")
@Parcelize
data class Note(
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "date") var dateMilliseconds: Long = Date().time,
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "folderId") var folderId: String = "",
    @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString()
) : Parcelable
