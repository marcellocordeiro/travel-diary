package com.myapp.traveldiary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myapp.traveldiary.MainActivity
import com.myapp.traveldiary.R
import com.myapp.traveldiary.dal.dao.Diary
import kotlinx.android.synthetic.main.diaries_list.view.*

class ListDiariesAdapter (private var diaries: List<Diary>, private val application: MainActivity):
    RecyclerView.Adapter<ListDiariesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val episodeView =
            LayoutInflater
                .from(application.applicationContext)
                .inflate(R.layout.diaries_list, parent, false)

        return ViewHolder(episodeView)
    }

    override fun getItemCount(): Int = diaries.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val diary = diaries[position]

        holder.name?.text = diary.name
    }

    // Pass a new list of episodes and notify
    fun updateDiariesList(diaries: List<Diary>) {
        this.diaries = diaries
        notifyItemInserted(diaries.size)
    }

    class ViewHolder (episodeView : View) : RecyclerView.ViewHolder(episodeView) {
        val name = episodeView.diary_name
    }
}