package com.example.myapplication.main.extension

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun String.isDateValid(format: String = "dd/MM/yyyy HH:mm"): Boolean {
    if (isEmpty()) return false
    val parser = SimpleDateFormat(format)
    parser.isLenient = false
    try {
        parser.parse(this)
    } catch (e: java.text.ParseException) {
        return false
    }
    return true
}

@SuppressLint("SimpleDateFormat")
fun String.toCalendar(format: String = "dd/MM/yyyy HH:mm"): Calendar {
    val parser = SimpleDateFormat(format)
    parser.isLenient = false
    val calendar = Calendar.getInstance()
    try {
        val date = parser.parse(this)
        calendar.time = date
    } catch (e: java.text.ParseException) {
        Log.d("okMAPPP", "error: $e")
    }
    return calendar
}

@SuppressLint("SimpleDateFormat")
fun Calendar.toStringEx(format: String = "dd/MM/yyyy HH:mm"): String {
    val formatter = SimpleDateFormat(format)
    return formatter.format(this.time)
}
