package com.example.myhomeworkoutlog.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_list_table")
data class Exercise(

    @PrimaryKey(autoGenerate = true)
    val exerciseId:Long = 0L,

    @ColumnInfo(name = "exercise_name")
    val exerciseName: String,

    @ColumnInfo(name = "exercise_type")
    val exerciseType: String
)