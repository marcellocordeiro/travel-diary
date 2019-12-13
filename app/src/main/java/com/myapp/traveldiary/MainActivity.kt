package com.myapp.traveldiary

import android.Manifest
import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.EditText
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
import java.util.*

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
        //val startDateInput: TextView = view.findViewById(R.id.start_date_text)
        //val endDateInput: TextView = view.findViewById(R.id.end_date_text)

        val startDateInput: EditText = view.findViewById(R.id.start_date)
        val endDateInput: EditText = view.findViewById(R.id.end_date)

        //val startDateButton: Button = view.findViewById(R.id.start_date_btn)
        //val endDateButton: Button = view.findViewById(R.id.end_date_btn)

        datePicker(startDateInput)
        datePicker(endDateInput)

        builder.apply {
            setPositiveButton(
                "OK"
            ) { _, _ ->
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
            ) { _, _ ->

            }

        }

        builder.create().show()
    }

    private fun datePicker(editText: EditText) {

        val c = Calendar.getInstance()

        val dayFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
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
                this@MainActivity,
                dateSetListener,
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}
