package com.myapp.traveldiary.dal

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.myapp.traveldiary.dal.dao.Diary
import com.myapp.traveldiary.dal.dao.DiaryDao

@Database(entities = [Diary::class], version = 1)
abstract class DiaryDB : RoomDatabase() {

    abstract fun diaryDao(): DiaryDao

    companion object {

        private var INSTANCE: DiaryDB? = null

        fun getInstance(context: Context): DiaryDB =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                DiaryDB::class.java,
                "diaries_name"
            ).build()
    }
}