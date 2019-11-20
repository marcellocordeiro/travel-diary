package com.myapp.traveldiary.dal.dao

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.myapp.traveldiary.dal.DiaryDB

class DiaryViewModel(application: Application) : AndroidViewModel(application) {

    val diaryList = DiaryDB.getInstance(application).diaryDao().selectAll()
}
