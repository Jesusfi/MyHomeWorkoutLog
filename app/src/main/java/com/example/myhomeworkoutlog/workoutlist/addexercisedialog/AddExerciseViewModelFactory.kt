package com.example.myhomeworkoutlog.workoutlist.addexercisedialog

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myhomeworkoutlog.database.ExerciseDao

class AddExerciseViewModelFactory(
    private val database: ExerciseDao,
    private val applicationContext: Application
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddExerciseViewModel::class.java)) {
            return AddExerciseViewModel(database, applicationContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}