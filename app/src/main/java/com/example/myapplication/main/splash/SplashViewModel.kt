package com.example.myapplication.main.splash

import android.app.Application
import com.example.myapplication.main.base.BaseViewModel
import com.example.myapplication.main.common.Constants
import com.example.myapplication.main.data.database.DBQrFood
import com.example.myapplication.main.data.database.model.Folder

/**
 * Created by nmduc3 on 1/6/21
 */
class SplashViewModel(application: Application) : BaseViewModel(application) {

    fun checkToGenerateData() {
        val database = DBQrFood.getInstance(getApplication())
        val folderDAO = database.folderDAO()
        val currentFolders = folderDAO.getAll()
        if (currentFolders.isEmpty()) {
            val listDefault = mutableListOf<Folder>()
            Constants.DefaultValue.DEFAULT_FOLDER.forEach { (key, value) ->
                listDefault.add(Folder(key, value))
            }
            folderDAO.insertFolders(*listDefault.toTypedArray())
        }
    }
}
