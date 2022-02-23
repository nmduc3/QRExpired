package com.example.myapplication.main.qrscan.result

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.main.base.BaseViewModel
import com.example.myapplication.main.data.database.DBQrFood
import com.example.myapplication.main.data.database.model.Folder
import com.example.myapplication.main.data.database.model.FoodInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultViewModel(application: Application): BaseViewModel(application) {
    private val database = DBQrFood.getInstance(application)
    private val foodDAO = database.foodInfoDAO()
    private val folderDAO = database.folderDAO()

    private val folderNameLD = MutableLiveData<Folder>()

    var errorString: String? = null
    var foodInfo: FoodInfo? = null

    fun folderNameLD(): LiveData<Folder> = folderNameLD

    fun saveFoodInfo(name: String) {
        foodInfo?.let {
            it.name = name
            foodDAO.insertFoods(it)
        }
    }

    fun getFolderName() {
        foodInfo?.let {
            viewModelScope.launch(Dispatchers.IO) {
                val folder = folderDAO.getFolderById(it.folderId)
                folderNameLD.postValue(folder)
            }
        }
    }
}
