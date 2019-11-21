package com.myapp.traveldiary.dal.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DiaryDao {

    @Query("SELECT * FROM diaries_name")
    fun selectAll(): LiveData<List<Diary>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(diary: Diary)
}
