package com.myapp.traveldiary

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

    private const val feedFormat = "EEE, d MMM yyyy HH:mm:ss Z"
    private val formatter = SimpleDateFormat(feedFormat, Locale.ENGLISH)
    private val dayFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

    fun parseToLong(string: String): Long {
        return dayFormatter.parse(string)?.time ?: Date(0).time
    }

    fun parseToString(context: Context, long: Long): String {
        val date = Date(long)
        val dateFormat = android.text.format.DateFormat.getDateFormat(context) as SimpleDateFormat
        val pattern = dateFormat
        return pattern.format(date)
    }


}