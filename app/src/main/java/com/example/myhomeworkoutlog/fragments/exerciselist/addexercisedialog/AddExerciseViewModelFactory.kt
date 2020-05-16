package com.example.myhomeworkoutlog.fragments.exerciselist.addexercisedialog

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myhomeworkoutlog.database.exercise.ExerciseDao

class AddExerciseViewModelFactory(
    private val database: ExerciseDao,
    private val applicationContext: Application
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddExerciseViewModel::class.java)) {
            return AddExerciseViewModel(
                database,
                applicationContext
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}