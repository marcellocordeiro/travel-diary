package com.myapp.traveldiary.dal.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diaries")
data class Diary(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "location") val location: String,
    @ColumnInfo(name = "start_date") val startDate: Long,
    @ColumnInfo(name = "end_date") val endDate: Long
) {

    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0

    override fun toString(): String {
        return "$name, $startDate, $endDate, $location"
    }
}
