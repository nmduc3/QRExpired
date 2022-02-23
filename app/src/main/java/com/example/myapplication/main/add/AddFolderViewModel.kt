package com.example.myapplication.main.add

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.main.base.BaseViewModel
import com.example.myapplication.main.data.database.DBQrFood
import com.example.myapplication.main.data.database.model.Folder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddFolderViewModel(application: Application) : BaseViewModel(application) {
    var database = DBQrFood.getInstance(application)
    var folderDAO = database.folderDAO()
    var folders = mutableListOf<Folder>()

    private val errorLiveData = MutableLiveData<Boolean>()

    fun errorLiveData(): LiveData<Boolean> = errorLiveData

    fun handleSaveNewFolder(id: String, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (folders.isEmpty()) {
                folders.addAll(folderDAO.getAll())
            }
            folders.forEach {
                if (it.id == id) {
                    errorLiveData.postValue(true)
                    return@launch
                }
            }
            folderDAO.insertFolders(Folder(id, name))
            errorLiveData.postValue(false)
        }
    }
}
