package com.myapp.traveldiary.dal.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="diaries_name")
data class Diary(@PrimaryKey(autoGenerate = true) val id: Long? = null,
                 @ColumnInfo(name = "name") val name: String) {

    override fun toString(): String {
        return "$id, $name"
    }
}