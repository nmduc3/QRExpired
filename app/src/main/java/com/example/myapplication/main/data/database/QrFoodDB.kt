package com.example.myapplication.main.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.main.data.database.dao.FolderDAO
import com.example.myapplication.main.data.database.dao.FoodInfoDAO
import com.example.myapplication.main.data.database.model.Folder
import com.example.myapplication.main.data.database.model.FoodInfo

@Database(entities = [Folder::class, FoodInfo::class], version = 1)
abstract class QrFoodDB : RoomDatabase() {
    abstract fun folderDAO(): FolderDAO
    abstract fun foodInfoDAO(): FoodInfoDAO
}
