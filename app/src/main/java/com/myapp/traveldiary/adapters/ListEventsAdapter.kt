package com.myapp.traveldiary.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myapp.traveldiary.DateHelper
import com.myapp.traveldiary.EventOverviewActivity
import com.myapp.traveldiary.R
import com.myapp.traveldiary.dal.AppDatabase
import com.myapp.traveldiary.dal.dao.Event
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk27.coroutines.onClick

class ListEventsAdapter(private val activity: EventOverviewActivity) :
    ListAdapter<Event, ListEventsAdapter.EventViewHolder>(DIFF_CALLBACK) {

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val image: ImageView = view.findViewById(R.id.event_image)
        private val name: TextView = view.findViewById(R.id.event_name)
        private val location: TextView = view.findViewById(R.id.location)
        private val startDate: TextView = view.findViewById(R.id.start_date)
        private val deleteButton: ImageView = view.findViewById(R.id.delete_button)

        private val eventDao = AppDatabase.getInstance(view.context).eventDao()

        fun bindTo(activity: EventOverviewActivity, item: Event) {
            deleteButton.apply {
                onClick {
                    doAsync {
                        eventDao.delete(item.uid)
                    }
                }
            }

            image.apply {
                if (item.imagePath == null) {
                    setImageResource(R.drawable.ic_add_box_red_800_36dp)
                } else {
                    setImageURI(Uri.parse(item.imagePath))
                }

                onClick {
                    val intent = Intent()
                    intent.type = "image/*"
                    intent.action = Intent.ACTION_PICK
                    EventOverviewActivity.eventUidQueue.add(item.uid)
                    activity.startActivityForResult(Intent.createChooser(intent, "Select photo"), 1)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = EventViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.entry_event,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bindTo(activity, getItem(position))
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Event>() {

            override fun areItemsTheSame(oldItem: Event, newItem: Event) =
                oldItem.uid == newItem.uid

            override fun areContentsTheSame(oldItem: Event, newItem: Event) =
                oldItem.imagePath == newItem.imagePath
        }
    }
}
