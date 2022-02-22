package com.example.myapplication.main.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "folder")
class Folder(
    @PrimaryKey
    @ColumnInfo(name = "id") var id: String,

    @ColumnInfo(name = "name") var name: String
)
