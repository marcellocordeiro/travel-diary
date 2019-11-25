package com.myapp.traveldiary.dal.dao

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.myapp.traveldiary.dal.AppDatabase

class EventViewModel(application: Application) : AndroidViewModel(application) {

    fun eventList(id: Long): LiveData<List<Event>> {
        return AppDatabase.getInstance(getApplication()).eventDao().selectAllEvents(id)
    }
}
