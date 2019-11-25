package com.myapp.traveldiary.dal.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diaries_name")
data class Diary(
    @ColumnInfo(name = "name") val name: String
) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    override fun toString(): String {
        return "$id, $name"
    }
}
