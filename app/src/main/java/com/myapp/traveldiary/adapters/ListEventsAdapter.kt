package com.myapp.traveldiary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myapp.traveldiary.R
import com.myapp.traveldiary.dal.dao.Event

class ListEventsAdapter : ListAdapter<Event, ListEventsAdapter.EventViewHolder>(DIFF_CALLBACK) {

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val name: TextView = view.findViewById(R.id.event_name)

        fun bindTo(item: Event) {
            name.apply {
                text = item.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = EventViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.entry_event,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    companion object {

        private const val EVENT_ID = "com.myapp.traveldiary.EVENT_ID"

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Event>() {

            override fun areItemsTheSame(oldItem: Event, newItem: Event) =
                oldItem.uid == newItem.uid

            override fun areContentsTheSame(oldItem: Event, newItem: Event) =
                oldItem.name == newItem.name
        }
    }
}
