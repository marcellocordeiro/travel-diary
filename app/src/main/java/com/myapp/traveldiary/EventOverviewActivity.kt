package com.myapp.traveldiary

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.myapp.traveldiary.adapters.ListEventsAdapter
import com.myapp.traveldiary.dal.AppDatabase
import com.myapp.traveldiary.dal.dao.Event
import com.myapp.traveldiary.dal.dao.EventViewModel
import kotlinx.android.synthetic.main.activity_event_overview.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk27.coroutines.onClick

class EventOverviewActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var createFab: FloatingActionButton
    private var diaryId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_overview)
        setSupportActionBar(toolbar)

        diaryId = intent.getLongExtra("com.myapp.traveldiary.DIARY_ID", -1)

        recyclerView = findViewById(R.id.recycler_view)
        createFab = findViewById(R.id.start_create_event)

        showEventList()

        createFab.onClick {
            showPopup()
        }
    }

    private fun showEventList() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ListEventsAdapter()
        }

        val model = ViewModelProviders.of(this).get(EventViewModel::class.java)
        model.eventList(diaryId).observe(
            this,
            Observer { (recyclerView.adapter as ListEventsAdapter).submitList(it) })
    }

    private fun showPopup() {
        val eventDb = AppDatabase.getInstance(applicationContext).eventDao()

        val view = layoutInflater.inflate(R.layout.popup_event_creation, null)

        val builder = AlertDialog.Builder(this).apply {
            setView(view)
        }

        val nameInput: TextInputEditText = view.findViewById(R.id.diary_name_input)
        val locationInput: TextInputEditText = view.findViewById(R.id.location_input)
        val dateInput: TextView = view.findViewById(R.id.date_text)

        val dateButton: Button = view.findViewById(R.id.date_btn)

        dateButton.onClick {
            datePicker(dateInput)
        }

        builder.apply {
            setPositiveButton(
                "OK"
            ) { dialog, id ->
                val name = nameInput.text.toString()
                val location = locationInput.text.toString()
                val startDate = DateHelper.parseToLong(dateInput.text.toString())
                val imagePath: String? = null

                val newEvent = Event(diaryId, name, location, startDate, imagePath)

                doAsync {
                    eventDb.insert(newEvent)
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
        val c = java.util.Calendar.getInstance()
        val year = c.get(java.util.Calendar.YEAR)
        val month = c.get(java.util.Calendar.MONTH)
        val day = c.get(java.util.Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                textView.text = "$dayOfMonth/" + (monthOfYear + 1) + "/$year"
            },
            year,
            month,
            day
        )
        dpd.show()
    }
}
