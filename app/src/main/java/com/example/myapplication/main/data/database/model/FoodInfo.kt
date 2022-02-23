package com.example.myapplication.main.data.database.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.main.extension.toCalendar
import com.example.myapplication.main.extension.toStringEx
import kotlinx.parcelize.Parcelize
import java.util.*

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
) : Parcelable {
    fun getDateExpiredInString(): String {
        val calendar = dateStart.toCalendar()
        calendar.add(Calendar.DATE, 3)
        return calendar.toStringEx()
    }

    fun getDateExpired(): Calendar {
        val calendar = dateStart.toCalendar()
        calendar.add(Calendar.DATE, 3)
        return calendar
    }
}