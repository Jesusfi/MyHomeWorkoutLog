package com.example.myhomeworkoutlog.workoutlist.contextmenudialog

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myhomeworkoutlog.database.ExerciseDao
import com.example.myhomeworkoutlog.workoutlist.ExerciseListViewModel

class ContextMenuListViewModelFactory(
    private val database: ExerciseDao,
    private val applicationContext: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContextMenuListViewModel::class.java)) {
            return ContextMenuListViewModel(database, applicationContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}