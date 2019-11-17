package com.myapp.traveldiary.tasks

import android.os.AsyncTask
import com.myapp.traveldiary.adapters.ListDiariesAdapter
import com.myapp.traveldiary.dal.DiaryDB
import com.myapp.traveldiary.dal.dao.Diary

class GetDiariesListTask(private val adapter: ListDiariesAdapter, private val db: DiaryDB) :
    AsyncTask<String, Int, List<Diary>>() {
    override fun doInBackground(vararg parameters: String?): List<Diary>? {
        return db.diaryDao().selectAll()
    }

    // Update list of diaries after getting from db
    override fun onPostExecute(result: List<Diary>) {
        adapter.updateDiariesList(result)
    }
}