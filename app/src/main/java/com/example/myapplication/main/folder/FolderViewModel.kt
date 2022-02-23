package com.example.myapplication.main.folder

import android.app.Application
import com.example.myapplication.main.base.BaseViewModel
import com.example.myapplication.main.data.database.DBQrFood
import com.example.myapplication.main.data.database.model.Folder

class FolderViewModel(application: Application): BaseViewModel(application) {
    var folder: Folder? = null
    private val database = DBQrFood.getInstance(application)
    private val foodDAO = database.foodInfoDAO()

    fun getAllFoodLD() = foodDAO.getAllByFolderIdLD(folder?.id)
}
