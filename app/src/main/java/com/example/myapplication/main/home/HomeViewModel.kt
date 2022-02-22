package com.example.myapplication.main.home

import android.app.Application
import com.example.myapplication.main.base.BaseViewModel
import com.example.myapplication.main.data.database.DBQrFood

class HomeViewModel(application: Application): BaseViewModel(application) {
    val database = DBQrFood.getInstance(application)
    val folderDAO = database.folderDAO()

    fun getALLFoldersLiveData() = folderDAO.getAllLD()
}
