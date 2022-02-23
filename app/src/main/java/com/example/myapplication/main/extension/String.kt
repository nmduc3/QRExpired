package com.example.myapplication.main.extension

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
fun String.isDateValid(format: String = "dd/MM/yyyy HH:mm"): Boolean {
    if (isEmpty()) return false
    val parser = SimpleDateFormat(format)
    parser.isLenient = false
    try {
        parser.parse(this)
    } catch (e: java.text.ParseException) {
        Log.d("okMAPPP", "parse error: $e")
        return false
    }
    return true
}
