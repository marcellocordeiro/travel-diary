package com.myapp.traveldiary.dal

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.myapp.traveldiary.dal.dao.Diary
import com.myapp.traveldiary.dal.dao.DiaryDao
import com.myapp.traveldiary.dal.dao.Event
import com.myapp.traveldiary.dal.dao.EventDao

@Database(entities = [Diary::class, Event::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun diaryDao(): DiaryDao
    abstract fun eventDao(): EventDao

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app.db"
            ).build()
    }
}
