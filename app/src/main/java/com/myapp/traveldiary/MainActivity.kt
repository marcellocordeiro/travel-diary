package com.myapp.traveldiary

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.myapp.traveldiary.adapters.ListDiariesAdapter
import com.myapp.traveldiary.dal.AppDatabase
import com.myapp.traveldiary.dal.dao.Diary
import com.myapp.traveldiary.dal.dao.DiaryViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.uiThread
import java.util.*

// Diary overview

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        showDiaryList()

        start_create_diary.onClick {
            showPopup()
        }
    }

    private fun showDiaryList() {
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ListDiariesAdapter()
        }

        val model = ViewModelProviders.of(this).get(DiaryViewModel::class.java)
        model.diaryList.observe(
            this,
            Observer { (recyclerView.adapter as ListDiariesAdapter).submitList(it) })
    }

    private fun showPopup() {
        val diaryDb = AppDatabase.getInstance(applicationContext).diaryDao()

        val dialog = Dialog(this).apply {
            setContentView(R.layout.popup_diary_creation)
        }

        val nameInput: TextInputEditText = dialog.findViewById(R.id.diary_name_input)
        val locationInput: TextInputEditText = dialog.findViewById(R.id.location_input)
        val startDateInput: TextView = dialog.findViewById(R.id.start_date_text)
        val endDateInput: TextView = dialog.findViewById(R.id.end_date_text)

        val startDateButton: Button = dialog.findViewById(R.id.start_date_btn)
        val endDateButton: Button = dialog.findViewById(R.id.end_date_btn)
        val confirmButton: Button = dialog.findViewById(R.id.create_diary)


        startDateButton.onClick {
            datePicker(startDateInput)
        }

        endDateButton.onClick {
            datePicker(endDateInput)
        }

        confirmButton.onClick {
            val name = nameInput.text.toString()
            val location = locationInput.text.toString()
            val startDate = DateHelper.parseToLong(startDateInput.text.toString())
            val endDate = DateHelper.parseToLong(endDateInput.text.toString())

            val newDiary = Diary(name, location, startDate, endDate)

            doAsync {
                diaryDb.insert(newDiary)

                uiThread {
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    private fun datePicker(textView: TextView) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

                // Display Selected date in TextView
                textView.text = "$dayOfMonth/" + (monthOfYear + 1) + "/$year"
            },
            year,
            month,
            day
        )
        dpd.show()
    }
}
