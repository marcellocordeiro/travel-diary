package com.myapp.traveldiary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myapp.traveldiary.DateHelper
import com.myapp.traveldiary.R
import com.myapp.traveldiary.dal.AppDatabase
import com.myapp.traveldiary.dal.dao.Event
import com.myapp.traveldiary.dal.dao.EventDao
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk27.coroutines.onClick

class ListEventsAdapter : ListAdapter<Event, ListEventsAdapter.EventViewHolder>(DIFF_CALLBACK) {

    private lateinit var eventDao: EventDao

    inner class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val image: ImageView = view.findViewById(R.id.event_image)
        private val name: TextView = view.findViewById(R.id.event_name)
        private val location: TextView = view.findViewById(R.id.location)
        private val startDate: TextView = view.findViewById(R.id.start_date)
        private val deleteButton: ImageView = view.findViewById(R.id.delete_button)

        fun bindTo(item: Event) {
            deleteButton.apply {
                onClick {
                    doAsync {
                        eventDao.delete(item.uid)
                    }
                }
            }

            image.apply {
                setImageResource(R.drawable.ic_add_box_red_800_36dp)

                onClick {
                    val newPath = if (item.imagePath != null) {
                        setImageResource(R.drawable.ic_add_box_red_800_36dp)
                        null
                    } else {
                        setImageResource(R.drawable.d2)
                        ""
                    }

                    doAsync {
                        eventDao.updateImagePath(item.uid, newPath)
                    }
                }
            }

            name.apply {
                text = item.name
            }

            location.apply {
                text = item.location
            }

            startDate.apply {
                text = DateHelper.parseToString(context, item.startDate)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        eventDao = AppDatabase.getInstance(parent.context).eventDao()

        return EventViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.entry_event,
                parent,
                false
            )
        )
    }

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
