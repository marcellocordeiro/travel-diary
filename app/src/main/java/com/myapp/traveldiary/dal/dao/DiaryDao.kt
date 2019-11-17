package com.myapp.traveldiary.dal.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DiaryDao  {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(diary: Diary)

    @Query("SELECT * FROM diaries_name")
    fun selectAll(): List<Diary>
}
