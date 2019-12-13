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

    @Query("SELECT COUNT(uid) FROM events WHERE diary_id = :uid")
    fun countTotal(uid: Long): Long

    @Query("SELECT COUNT(uid) FROM events WHERE diary_id = :uid AND image_path NOT NULL")
    fun countCompleted(uid: Long): Long

    @Query("UPDATE events SET image_path = :path WHERE uid = :uid")
    fun updateImagePath(uid: Long, path: String?)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(item: Event)

    @Query("DELETE FROM events WHERE uid = :uid")
    fun delete(uid: Long)

    @Query("DELETE FROM events WHERE diary_id = :diaryUid")
    fun deleteAll(diaryUid: Long)
}
