package com.example.myapplication.main.extension

import android.content.Context
import android.content.res.Configuration
import android.os.SystemClock
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
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

fun Context.isPortrait() : Boolean {
    val orientation = this.resources.configuration.orientation
    return orientation == Configuration.ORIENTATION_PORTRAIT
}

fun View.gone() {
    if (!isGone) visibility = View.GONE
}

fun View.invisible() {
    if (!isInvisible) visibility = View.INVISIBLE
}

fun View.visible() {
    if (!isVisible) visibility = View.VISIBLE
}
