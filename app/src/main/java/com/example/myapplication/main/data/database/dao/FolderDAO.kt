package com.example.myapplication.main.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.main.data.database.model.Folder

@Dao
interface FolderDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFolders(vararg folder: Folder)

    @Query("SELECT * FROM folder")
    fun getAll(): List<Folder>

    @Query("SELECT * FROM folder")
    fun getAllLD(): LiveData<List<Folder>>
}
