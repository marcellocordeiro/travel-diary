package com.myapp.traveldiary

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.widget.EditText
import org.jetbrains.anko.sdk27.coroutines.onClick
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

    fun datePicker(context: Context, editText: EditText) {

        val c = Calendar.getInstance()

        val dayFormatter = android.icu.text.SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        editText.setText(dayFormatter.format(c.time))

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                c.set(Calendar.YEAR, year)
                c.set(Calendar.MONTH, monthOfYear)
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                editText.setText(dayFormatter.format(c.time))
            }

        editText.onClick {
            DatePickerDialog(
                context,
                dateSetListener,
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}
