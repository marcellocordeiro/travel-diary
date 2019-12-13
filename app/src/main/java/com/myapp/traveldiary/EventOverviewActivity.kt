package com.myapp.traveldiary

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.myapp.traveldiary.adapters.ListEventsAdapter
import com.myapp.traveldiary.dal.AppDatabase
import com.myapp.traveldiary.dal.dao.Event
import com.myapp.traveldiary.dal.dao.EventViewModel
import kotlinx.android.synthetic.main.activity_event_overview.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class EventOverviewActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var createFab: FloatingActionButton
    private var diaryId: Long = -1

    companion object {

        val eventUidQueue: Queue<Long> = ArrayDeque<Long>()
    }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val eventUid = eventUidQueue.remove()
            val uri = data?.data

            val diaryDao = AppDatabase.getInstance(application).diaryDao()
            val eventDao = AppDatabase.getInstance(application).eventDao()

            doAsync {
                eventDao.updateImagePath(eventUid, uri.toString())

                val total = eventDao.countTotal(diaryId)
                val totalCompleted = eventDao.countCompleted(diaryId)
                diaryDao.updateCompletion(diaryId, total == totalCompleted)
            }
        }
    }

    private fun showEventList() {
        val activity = this
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ListEventsAdapter(activity)
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

        val dateInput: EditText = view.findViewById(R.id.date)

        datePicker(dateInput)


        builder.apply {
            setPositiveButton(
                "OK"
            ) { _, _ ->
                val name = nameInput.text.toString()
                val location = locationInput.text.toString()
                val startDate = DateHelper.parseToLong(dateInput.text.toString())
                val imagePath: String? = null

                val newEvent = Event(diaryId, name, location, startDate, imagePath)

                doAsync {
                    eventDb.insert(newEvent)

                    val diaryDao = AppDatabase.getInstance(applicationContext).diaryDao()
                    diaryDao.updateCompletion(diaryId, false)
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
                this@EventOverviewActivity,
                dateSetListener,
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}
