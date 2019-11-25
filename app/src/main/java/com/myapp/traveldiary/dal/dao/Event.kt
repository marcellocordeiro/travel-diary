package com.myapp.traveldiary.dal.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "events", foreignKeys = [ForeignKey(entity = Diary::class, parentColumns = ["uid"], childColumns = ["diary_id"])])
data class Event(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "diary_id") val diaryId: Long
) {

    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0


    override fun toString(): String {
        return "$uid, $name"
    }
}
