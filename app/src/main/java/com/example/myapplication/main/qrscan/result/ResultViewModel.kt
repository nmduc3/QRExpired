package com.example.myapplication.main.qrscan.result

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.myapplication.main.alarm.AlarmWorker
import com.example.myapplication.main.base.BaseViewModel
import com.example.myapplication.main.common.Constants
import com.example.myapplication.main.common.Event
import com.example.myapplication.main.data.database.DBQrFood
import com.example.myapplication.main.data.database.model.Folder
import com.example.myapplication.main.data.database.model.FoodInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class ResultViewModel(application: Application) : BaseViewModel(application) {
    private val database = DBQrFood.getInstance(application)
    private val foodDAO = database.foodInfoDAO()
    private val folderDAO = database.folderDAO()

    private val folderNameLD = MutableLiveData<Folder>()
    private val expiredFoodLD = MutableLiveData<Event<String>>()

    var errorString: String? = null
    var foodInfo: FoodInfo? = null

    fun folderNameLD(): LiveData<Folder> = folderNameLD

    fun expiredFoodLD(): LiveData<Event<String>> = expiredFoodLD

    fun saveFoodInfo(name: String) {
        foodInfo?.let {
            it.name = name
            foodDAO.insertFoods(it)
            handleAlarm(it)
        }
    }

    private fun handleAlarm(foodInfo: FoodInfo) {
        val expireDay = foodInfo.getDateExpired()
        val today = Calendar.getInstance()
        if (expireDay.before(Calendar.getInstance())) {
            expiredFoodLD.postValue(Event(foodInfo.getDateExpiredInString()))
        } else {
            val workRequest = OneTimeWorkRequestBuilder<AlarmWorker>()
                .setInitialDelay(expireDay.timeInMillis - today.timeInMillis, TimeUnit.MILLISECONDS)
                .setInputData(
                    workDataOf(
                        Constants.DefaultValue.KEY_NAME to foodInfo.name
                    )
                )
                .build()
            WorkManager.getInstance(getApplication())
                .enqueueUniqueWork(foodInfo.foodId, ExistingWorkPolicy.REPLACE, workRequest)
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
