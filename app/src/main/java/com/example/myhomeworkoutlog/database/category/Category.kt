package com.example.myhomeworkoutlog.database.category

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_list_table")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val categoryId: Long = 0L,

    @ColumnInfo(name = "category_name")
    var categoryName: String
)