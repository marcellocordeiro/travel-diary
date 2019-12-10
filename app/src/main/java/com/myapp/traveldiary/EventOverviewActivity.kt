package com.myapp.traveldiary

import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
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

        createFab.setOnClickListener { view ->
            createPopUpWindow(view)
        }
    }

    private fun showEventList() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ListEventsAdapter()
            //setHasFixedSize(true)
        }

        val model = ViewModelProviders.of(this).get(EventViewModel::class.java)
        model.eventList(diaryId).observe(
            this,
            Observer { (recyclerView.adapter as ListEventsAdapter).submitList(it) })
    }

    private fun createPopUpWindow(view: View) {
        val inflater: LayoutInflater =
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.activity_diary_creation, null)
        val layout = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        val popupWindow = PopupWindow(popupView, layout, layout, focusable)

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        createDiaryOnCreateDiaryButtonClick(popupView, popupWindow)
    }

    private fun createDiaryOnCreateDiaryButtonClick(view: View, popupWindow: PopupWindow) {
        view.findViewById<Button>(R.id.create_diary).setOnClickListener {
            val diaryName =
                view.findViewById<TextInputEditText>(R.id.diary_name_input).text.toString()

            doAsync {
                val eventDb = AppDatabase.getInstance(applicationContext).eventDao()

                eventDb.insert(Event(diaryName, diaryId, Calendar.getInstance().time.time, "my description", "my location"))
            }

            popupWindow.dismiss()
        }
    }
}
