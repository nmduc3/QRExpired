package com.example.myapplication.main.data.database.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "food_info")
@Parcelize
class FoodInfo(
    @PrimaryKey
    @ColumnInfo(name = "food_id")
    var foodId: String = "",

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "folder_id")
    var folderId: String = "",

    @ColumnInfo(name = "date_start")
    var dateStart: String = ""
): Parcelable {
    fun getDateExpired(): String {
        return "TODO"
    }
}