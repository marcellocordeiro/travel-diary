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

        fun getDatabase(ctx: Context): DiaryDB {
            if (INSTANCE == null) {
                synchronized(DiaryDB::class) {
                    INSTANCE = Room.databaseBuilder(
                        ctx.applicationContext,
                        DiaryDB::class.java,
                        "diaries_name")
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}