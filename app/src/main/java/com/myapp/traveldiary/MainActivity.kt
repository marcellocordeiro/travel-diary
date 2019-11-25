package com.myapp.traveldiary

import android.content.Context
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
import org.jetbrains.anko.sdk27.coroutines.onClick

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
    }

    private fun createDiaryOnCreateDiaryButtonClick(view: View, popupWindow: PopupWindow) {
        view.findViewById<Button>(R.id.create_diary).setOnClickListener {
            val diaryName =
                view.findViewById<TextInputEditText>(R.id.diary_name_input).text.toString()

            doAsync {
                val diaryDB = AppDatabase.getInstance(applicationContext)
                val diary = Diary(name = diaryName)

                diaryDB.diaryDao().insert(diary)
            }

            popupWindow.dismiss()
        }
    }
}
