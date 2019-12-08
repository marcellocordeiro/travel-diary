package com.myapp.traveldiary

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.myapp.traveldiary.adapters.ListDiariesAdapter
import com.myapp.traveldiary.dal.AppDatabase
import com.myapp.traveldiary.dal.dao.Diary
import com.myapp.traveldiary.dal.dao.DiaryViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import java.util.*

// Diary overview

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.recycler_view)

        showDiaryList()

        start_create_diary.setOnClickListener { view ->
            createPopUpWindow(view)
        }
    }

    private fun showDiaryList() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ListDiariesAdapter()
            //setHasFixedSize(true)

            addItemDecoration(
                DividerItemDecoration(
                    context,
                    (layoutManager as LinearLayoutManager).orientation
                )
            )
        }

        val model = ViewModelProviders.of(this).get(DiaryViewModel::class.java)
        model.diaryList.observe(
            this,
            Observer { (recyclerView.adapter as ListDiariesAdapter).submitList(it) })
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
        chooseStartDate(popupView)
        chooseEndDate(popupView)
    }

    private fun createDiaryOnCreateDiaryButtonClick(view: View, popupWindow: PopupWindow) {
        view.findViewById<Button>(R.id.create_diary).setOnClickListener {
            val diaryName =
                view.findViewById<TextInputEditText>(R.id.diary_name_input).text.toString()
            val location = view.findViewById<TextInputEditText>(R.id.location_input).text.toString()
            val startDateText = view.findViewById<TextView>(R.id.start_date_text).text.toString()
            val endDateText = view.findViewById<TextView>(R.id.end_date_text).text.toString()

            val startDate = DateHelper.parseToLong(startDateText)
            val endDate = DateHelper.parseToLong(endDateText)

            doAsync {
                val diaryDB = AppDatabase.getInstance(applicationContext)
                val diary = Diary(name = diaryName, startDate = startDate, endDate = endDate, location = location)

                diaryDB.diaryDao().insert(diary)
            }

            popupWindow.dismiss()
        }
    }

    private fun chooseStartDate(view: View) {
        val mPickTimeBtn = view.findViewById<Button>(R.id.start_date_btn)
        val textView     = view.findViewById<TextView>(R.id.start_date_text)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        mPickTimeBtn.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                textView.text = "$dayOfMonth/$month/$year"
            }, year, month, day)
            dpd.show()

        }
    }

    private fun chooseEndDate(view: View) {
        val mPickTimeBtn = view.findViewById<Button>(R.id.end_date_btn)
        val textView     = view.findViewById<TextView>(R.id.end_date_text)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        mPickTimeBtn.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                textView.text = "$dayOfMonth/$month/$year"
            }, year, month, day)
            dpd.show()

        }
    }
}
