package com.myapp.traveldiary

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

    private val dayFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

    fun parseToLong(string: String): Long {
        return dayFormatter.parse(string)?.time ?: Date(0).time
    }

    fun parseToString(context: Context, long: Long): String {
        val date = Date(long)
        val dateFormat = android.text.format.DateFormat.getDateFormat(context) as SimpleDateFormat
        return dateFormat.format(date)
    }
}
