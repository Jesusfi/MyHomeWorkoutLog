package com.example.myhomeworkoutlog.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ExerciseDao{
    @Insert
    fun insert(exercise: Exercise)

    @Update
    fun update(exercise: Exercise)

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM exercise_list_table")
    fun clear()

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by start time in descending order.
     */
    @Query("SELECT * FROM exercise_list_table ORDER BY exerciseId DESC")
    fun getAllExercises(): LiveData<List<Exercise>>


    /**
     * Selects and returns the exercise with given exerciseId.
     */
    @Query("SELECT * from exercise_list_table WHERE exerciseId = :key")
    fun getNightWithId(key: Long): LiveData<Exercise>
}