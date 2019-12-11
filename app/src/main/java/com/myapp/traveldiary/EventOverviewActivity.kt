package com.myapp.traveldiary

import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
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
import org.jetbrains.anko.uiThread


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

        val dialog = Dialog(this).apply {
            setContentView(R.layout.popup_event_creation)
        }

        val confirmButton: Button = dialog.findViewById(R.id.create_diary)
        val nameInput: TextInputEditText = dialog.findViewById(R.id.diary_name_input)

        confirmButton.onClick {
            val name = nameInput.text.toString()
            val location = "my location"
            val startDate = Calendar.getInstance().time.time
            val imagePath: String? = null

            val newEvent = Event(diaryId, name, location, startDate, imagePath)

            doAsync {
                eventDb.insert(newEvent)

                uiThread {
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }
}
