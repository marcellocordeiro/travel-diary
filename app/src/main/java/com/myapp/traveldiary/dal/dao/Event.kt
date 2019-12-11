package com.myapp.traveldiary.dal.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "events",
    foreignKeys = [ForeignKey(
        entity = Diary::class,
        parentColumns = ["uid"],
        childColumns = ["diary_id"]
    )]
)
data class Event(
    @ColumnInfo(name = "diary_id") val diaryId: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "location") val location: String,
    @ColumnInfo(name = "start_date") val startDate: Long,
    @ColumnInfo(name = "image_path") val imagePath: String?
) {

    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0


    override fun toString(): String {
        return "$uid, $name"
    }
}
