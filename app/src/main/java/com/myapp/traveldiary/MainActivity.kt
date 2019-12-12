package com.myapp.traveldiary

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
import pub.devrel.easypermissions.EasyPermissions


// Diary overview

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val permission = Manifest.permission.READ_EXTERNAL_STORAGE

        if (EasyPermissions.hasPermissions(this, permission)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Read photos", 0, permission)
        }

        showDiaryList()

        start_create_diary.onClick {
            showPopup()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
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

        val view = layoutInflater.inflate(R.layout.popup_diary_creation, null)

        val builder = AlertDialog.Builder(this).apply {
            setView(view)
        }

        val nameInput: TextInputEditText = view.findViewById(R.id.diary_name_input)
        val locationInput: TextInputEditText = view.findViewById(R.id.location_input)
        val startDateInput: TextView = view.findViewById(R.id.start_date_text)
        val endDateInput: TextView = view.findViewById(R.id.end_date_text)

        val startDateButton: Button = view.findViewById(R.id.start_date_btn)
        val endDateButton: Button = view.findViewById(R.id.end_date_btn)

        startDateButton.onClick {
            datePicker(startDateInput)
        }

        endDateButton.onClick {
            datePicker(endDateInput)
        }

        builder.apply {
            setPositiveButton(
                "OK"
            ) { dialog, id ->
                val name = nameInput.text.toString()
                val location = locationInput.text.toString()
                val startDate = DateHelper.parseToLong(startDateInput.text.toString())
                val endDate = DateHelper.parseToLong(endDateInput.text.toString())

                val newDiary = Diary(name, location, startDate, endDate)

                doAsync {
                    diaryDb.insert(newDiary)
                }
            }

            setNegativeButton(
                "Cancel"
            ) { dialog, id ->

            }

        }

        builder.create().show()
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
