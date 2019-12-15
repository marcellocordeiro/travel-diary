package com.myapp.traveldiary

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
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

            doAsync {
                val diaryDao = AppDatabase.getInstance(application).diaryDao()
                val eventDao = AppDatabase.getInstance(application).eventDao()

                eventDao.updateImagePath(eventUid, uri.toString())

                val total = eventDao.countTotal(diaryId)
                val totalCompleted = eventDao.countCompleted(diaryId)
                diaryDao.updateCompletion(diaryId, total == totalCompleted)
            }
        }
    }

    private fun showEventList() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ListEventsAdapter(this@EventOverviewActivity)
        }

        val model = ViewModelProviders.of(this).get(EventViewModel::class.java)
        model.eventList(diaryId).observe(
            this,
            Observer { (recyclerView.adapter as ListEventsAdapter).submitList(it) })
    }

    @SuppressLint("InflateParams")
    private fun showPopup() {
        val content = LayoutInflater.from(this).inflate(R.layout.popup_event_creation, null)

        val nameInput: TextInputEditText = content.findViewById(R.id.diary_name_input)
        val locationInput: TextInputEditText = content.findViewById(R.id.location_input)
        val dateInput: EditText = content.findViewById(R.id.date)

        DateHelper.datePicker(this, dateInput)

        val positiveCallback = {
            val name = nameInput.text.toString()
            val location = locationInput.text.toString()
            val startDate = DateHelper.parseToLong(dateInput.text.toString())
            val imagePath: String? = null

            val newEvent = Event(diaryId, name, location, startDate, imagePath)

            doAsync {
                val eventDb = AppDatabase.getInstance(applicationContext).eventDao()
                val diaryDao = AppDatabase.getInstance(applicationContext).diaryDao()

                eventDb.insert(newEvent)
                diaryDao.updateCompletion(diaryId, false)
            }
        }

        val popup = PopupFragment(content, positiveCallback, {})
        popup.show(supportFragmentManager, "Event")
    }
}
