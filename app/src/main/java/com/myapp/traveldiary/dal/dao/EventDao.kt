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

    @Query("UPDATE events SET image_path = :path WHERE uid = :uid")
    fun updateImagePath(uid: Long, path: String?)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(item: Event)

    @Query("DELETE FROM events WHERE uid = :uid")
    fun delete(uid: Long)
}
