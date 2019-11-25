package com.myapp.traveldiary.dal.dao

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.myapp.traveldiary.dal.AppDatabase

class DiaryViewModel(application: Application) : AndroidViewModel(application) {

    val diaryList = AppDatabase.getInstance(application).diaryDao().selectAll()
}
