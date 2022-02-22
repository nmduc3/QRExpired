package com.example.myapplication.main.extension

import android.os.SystemClock
import android.view.View
import com.example.myapplication.main.common.Constants

fun View.disableMultipleClick(
    timeDelay: Long = Constants.DefaultValue.TIME_DELAY_DEFAULT,
    eventClick: (view: View) -> Unit
) {
    var timeNow = 0L
    setOnClickListener {
        SystemClock.elapsedRealtime().run {
            if (this - timeNow < timeDelay) {
                return@setOnClickListener
            }
            eventClick.invoke(it)
            timeNow = this
        }
    }
}