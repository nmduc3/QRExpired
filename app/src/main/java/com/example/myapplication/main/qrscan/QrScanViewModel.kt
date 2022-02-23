package com.example.myapplication.main.qrscan

import android.app.Application
import com.example.myapplication.main.base.BaseViewModel
import com.example.myapplication.main.data.database.DBQrFood
import com.example.myapplication.main.data.database.model.FoodInfo
import com.example.myapplication.main.extension.isDateValid

class QrScanViewModel(application: Application) : BaseViewModel(application) {
    val database = DBQrFood.getInstance(application)
    val foodDAO = database.foodInfoDAO()

    var isQrHasDetected = false

    fun handleQrData(qrString: String): FoodInfo? {
        val foodInfo = FoodInfo()
        val split1 = qrString.split(" ")
        if (split1.size == 2) {
            val group1 = split1[0].split(":")
            val group2 = split1[1]
            if (group1.size == 2) {
                foodInfo.folderId = group1[0]
                foodInfo.foodId = group1[1]
            }
            if (group2.isNotEmpty()) {
                foodInfo.dateStart = group2.replace("_", " ")
            }
        }
        val result = foodInfo.run {
            folderId.isNotEmpty() && foodId.isNotEmpty() && dateStart.isDateValid()
        }
        return if (result) foodInfo else null
    }
}
