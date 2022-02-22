package com.example.myapplication.main.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.main.data.database.model.Folder
import com.example.myapplication.main.data.database.model.FoodInfo

@Dao
interface FoodInfoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFolders(vararg folder: FoodInfo)

    @Query("SELECT * FROM food_info")
    fun getAll(): List<FoodInfo>

    @Query("SELECT * FROM food_info")
    fun getAllLD(): LiveData<List<FoodInfo>>
}
