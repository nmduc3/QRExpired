package com.example.myapplication.main.data.database

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.myapplication.main.common.Constants

class DBQrFood {
    companion object {
        private lateinit var instance: QrFoodDB
        fun getInstance(context: Context): QrFoodDB {
            if (!::instance.isInitialized) {
                instance = Room.databaseBuilder(context, QrFoodDB::class.java, Constants.DefaultValue.DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }
}
