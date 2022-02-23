package com.example.myapplication.main.data.database.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "folder")
@Parcelize
class Folder(
    @PrimaryKey
    @ColumnInfo(name = "id") var id: String,

    @ColumnInfo(name = "name") var name: String
): Parcelable
