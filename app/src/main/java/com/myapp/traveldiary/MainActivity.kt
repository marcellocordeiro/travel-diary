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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.myapp.traveldiary.adapters.ListDiariesAdapter
import com.myapp.traveldiary.dal.DiaryDB
import com.myapp.traveldiary.dal.dao.Diary
import com.myapp.traveldiary.tasks.GetDiariesListTask
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync


// Diary overview

class MainActivity : AppCompatActivity() {
    private var diaryDB: DiaryDB? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ListDiariesAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        diaryDB = DiaryDB.getDatabase(this)

        viewManager = LinearLayoutManager(this)
        viewAdapter = ListDiariesAdapter(emptyList(), this)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))


        GetDiariesListTask(viewAdapter, diaryDB!!).execute()

        start_create_diary.setOnClickListener { view ->
            createPopUpWindow(view)

        }
    }

    private fun createPopUpWindow(view: View) {
        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
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
            val diaryName = view.findViewById<TextInputEditText>(R.id.diary_name_input).text.toString()

            doAsync {
                val diary = Diary(name = diaryName)

                diaryDB!!.diaryDao().insert(diary)

                GetDiariesListTask(viewAdapter, diaryDB!!).execute()
            }

            popupWindow.dismiss()
        }
    }
}
