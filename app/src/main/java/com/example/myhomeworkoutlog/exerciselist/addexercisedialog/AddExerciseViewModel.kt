package com.example.myhomeworkoutlog.exerciselist.addexercisedialog

import android.app.Application
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myhomeworkoutlog.database.Exercise
import com.example.myhomeworkoutlog.database.ExerciseDao
import kotlinx.coroutines.*

class AddExerciseViewModel(
    private val database: ExerciseDao,
    application: Application
) :
    AndroidViewModel(application) {

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()


    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this uiScope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    private val _userInputNothingForExerciseNameEvent = MutableLiveData<Boolean>()
    val userInputNothingForExerciseNameEvent: LiveData<Boolean>
        get() = _userInputNothingForExerciseNameEvent

    private val _exitDialogEventOnActionComplete = MutableLiveData<Boolean>()
    val exitDialogEventOnActionComplete: LiveData<Boolean>
        get() = _exitDialogEventOnActionComplete

    init {
        _userInputNothingForExerciseNameEvent.value = false
        _exitDialogEventOnActionComplete.value = false
    }

    private fun createAndSaveNewExercise(exerciseName: String, exerciseType: String) {
        Log.d("AddExercise", "reached create new exercise")
        val exercise = Exercise(exerciseName = exerciseName, exerciseType = exerciseType)

        uiScope.launch {
            Log.d("AddExercise", "in uiScope")
            insert(exercise)
            _exitDialogEventOnActionComplete.value = true
        }
    }

    private suspend fun insert(exercise: Exercise) {
        withContext(Dispatchers.IO) {
            database.insert(exercise)
        }
    }

    private fun onUserInputNothing() {
        _userInputNothingForExerciseNameEvent.value = true
    }

    fun onUserClickDialogButton(exerciseName: String, exerciseType: String, inputType: Int) {
        if (TextUtils.isEmpty(exerciseName)) {
            onUserInputNothing()
        } else if (inputType == 1) {
            createAndSaveNewExercise(exerciseName, exerciseType)
        } else {

        }
    }

    fun finishedExitingDialog() {
        _exitDialogEventOnActionComplete.value = false
    }

    fun finishedNotifyingUserInputtedNothing() {
        _userInputNothingForExerciseNameEvent.value = false
    }




}