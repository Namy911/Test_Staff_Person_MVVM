package com.example.myapplication.ui.util

import java.text.SimpleDateFormat
import java.util.*

class MyUtil {

    internal fun convertBtnText(dateStamp: Long): String {
        val sdf = SimpleDateFormat("MM / dd / yyyy")
        val netDate = Date(dateStamp)
        return sdf.format(netDate)
    }
}