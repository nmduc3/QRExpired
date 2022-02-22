package com.example.myapplication.main.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_info")
class FoodInfo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "folder_id")
    var folderId: String = "",

    @ColumnInfo(name = "food_id")
    var foodId: String = "",

    @ColumnInfo(name = "date_start")
    var dateStart: String = ""
)