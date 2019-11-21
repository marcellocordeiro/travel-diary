package com.myapp.traveldiary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myapp.traveldiary.R
import com.myapp.traveldiary.dal.dao.Diary

class ListDiariesAdapter : ListAdapter<Diary, ListDiariesAdapter.DiaryViewHolder>(DIFF_CALLBACK) {

    class DiaryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val name: TextView = view.findViewById(R.id.diary_name)

        fun bindTo(item: Diary) {
            name.apply {
                text = item.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DiaryViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.diary_entry,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bindTo(item)
    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Diary>() {

            override fun areItemsTheSame(oldItem: Diary, newItem: Diary) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Diary, newItem: Diary) =
                oldItem.name == newItem.name
        }
    }
}
