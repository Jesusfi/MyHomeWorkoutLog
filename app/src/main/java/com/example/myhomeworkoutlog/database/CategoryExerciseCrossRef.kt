package com.example.myhomeworkoutlog.database

import androidx.room.Entity

@Entity(primaryKeys = ["categoryId", "exerciseId"])
data class CategoryExerciseCrossRef(
    val categoryId: Long,
    val exerciseId: Long
)