package com.example.myhomeworkoutlog.fragments.exerciselist.updateexercisedialog

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myhomeworkoutlog.database.ExerciseDao

class UpdateExerciseViewModelFactory(
    private val database: ExerciseDao,
    private val applicationContext: Application,
    private val exerciseId: Long
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdateExerciseViewModel::class.java)) {
            return UpdateExerciseViewModel(
                database,
                applicationContext,
                exerciseId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}