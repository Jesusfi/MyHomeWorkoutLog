package com.example.myhomeworkoutlog.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.myhomeworkoutlog.database.category.Category
import com.example.myhomeworkoutlog.database.exercise.Exercise


data class CategoryWithExercise(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "exerciseId",
        associateBy = Junction(CategoryExerciseCrossRef::class)
    )
    val exercises: List<Exercise>
)