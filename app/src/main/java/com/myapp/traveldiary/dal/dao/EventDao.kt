package com.myapp.traveldiary.dal.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EventDao {

    @Query("SELECT * FROM events WHERE diary_id = :uid")
    fun selectAllEvents(uid: Long): LiveData<List<Event>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(item: Event)
}
