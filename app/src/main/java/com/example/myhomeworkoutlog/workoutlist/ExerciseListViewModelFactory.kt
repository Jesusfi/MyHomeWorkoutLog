package com.example.myhomeworkoutlog.workoutlist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myhomeworkoutlog.database.ExerciseDao

class ExerciseListViewModelFactory(
    private val database: ExerciseDao,
    private val applicationContext: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExerciseListViewModel::class.java)) {
            return ExerciseListViewModel(database, applicationContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}