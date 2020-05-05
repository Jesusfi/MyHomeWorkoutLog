package com.example.myhomeworkoutlog.mainworkout

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainFragmentViewModel : ViewModel() {
    private val _navigateToWorkoutListEvent = MutableLiveData<Boolean>()
    val navigateToWorkoutListEvent: LiveData<Boolean>
        get() = _navigateToWorkoutListEvent

    init {
        _navigateToWorkoutListEvent.value = false
    }


    fun userClickedWorkoutListButton() {
        _navigateToWorkoutListEvent.value = true

    }

    fun navigateToWorkoutListEventComplete() {
        _navigateToWorkoutListEvent.value = false
    }

    override fun onCleared() {
        super.onCleared()
    }
}