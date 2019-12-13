package com.myapp.traveldiary.dal.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DiaryDao {

    @Query("SELECT * FROM diaries")
    fun selectAll(): LiveData<List<Diary>>

    @Query("UPDATE diaries SET completed = :completed WHERE uid = :uid")
    fun updateCompletion(uid: Long, completed: Boolean)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(diary: Diary)

    @Query("DELETE FROM diaries WHERE uid = :uid")
    fun delete(uid: Long)
}
