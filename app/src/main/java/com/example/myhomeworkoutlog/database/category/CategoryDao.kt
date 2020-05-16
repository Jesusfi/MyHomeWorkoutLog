package com.example.myhomeworkoutlog.database.category

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myhomeworkoutlog.database.CategoryWithExercise
import com.example.myhomeworkoutlog.database.exercise.Exercise

@Dao
interface CategoryDao{

    @Insert
    fun insert(category: Category)

    @Update
    fun update(category: Category)

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM category_list_table")
    fun clear()


    /**
     * Selects and returns all rows in the table,
     *
     * sorted by start time in descending order.
     */
    @Query("SELECT * FROM category_list_table ORDER BY categoryId DESC")
    fun getAllCategories(): LiveData<List<Category>>


    /**
     * Selects and returns the category with given categoryId.
     */
    @Query("SELECT * from category_list_table WHERE categoryId = :key")
    fun getCategoryWithId(key: Long): LiveData<Category>

    /**
     * Deletes exercise with given categoryId
     */
    @Query("DELETE FROM category_list_table WHERE categoryId = :key")
    fun deleteByCategoryId(key: Long)

    @Transaction
    @Query("SELECT * FROM category_list_table")
    fun getCategoryWithExercises(): List<CategoryWithExercise>


}