package com.myapp.traveldiary

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
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

    @SuppressLint("InflateParams")
    private fun showPopup() {
        val content = LayoutInflater.from(this).inflate(R.layout.popup_diary_creation, null)

        val nameInput: TextInputEditText = content.findViewById(R.id.diary_name_input)
        val locationInput: TextInputEditText = content.findViewById(R.id.location_input)
        val startDateInput: EditText = content.findViewById(R.id.start_date)
        val endDateInput: EditText = content.findViewById(R.id.end_date)

        DateHelper.datePicker(this, startDateInput)
        DateHelper.datePicker(this, endDateInput)

        val positiveCallback = {
            val name = nameInput.text.toString()
            val location = locationInput.text.toString()
            val startDate = DateHelper.parseToLong(startDateInput.text.toString())
            val endDate = DateHelper.parseToLong(endDateInput.text.toString())

            val newDiary = Diary(name, location, startDate, endDate)

            doAsync {
                val diaryDb = AppDatabase.getInstance(applicationContext).diaryDao()
                diaryDb.insert(newDiary)
            }
        }

        val popup = PopupFragment(content, positiveCallback, {})
        popup.show(supportFragmentManager, "Diary")
    }
}
