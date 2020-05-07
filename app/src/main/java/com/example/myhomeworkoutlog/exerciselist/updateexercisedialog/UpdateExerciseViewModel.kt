package com.example.myhomeworkoutlog.exerciselist.updateexercisedialog

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myhomeworkoutlog.database.Exercise
import com.example.myhomeworkoutlog.database.ExerciseDao
import kotlinx.coroutines.*

class UpdateExerciseViewModel(
    private val database: ExerciseDao,
    application: Application,
    exerciseId: Long
) : AndroidViewModel(application) {
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

    val exerciseToUpdate: LiveData<Exercise> = database.getExerciseWithId(exerciseId)

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

    fun onUpdateExercise(exerciseToUpdate: Exercise, exerciseName: String, exerciseType: String) {

        if (TextUtils.isEmpty(exerciseName)) {
            onUserInputNothing()
        } else {
            val updatedExercise = exerciseToUpdate
            updatedExercise.exerciseName = exerciseName
            updatedExercise.exerciseType = exerciseType

            uiScope.launch {
                update(updatedExercise)
                _exitDialogEventOnActionComplete.value = true
            }
        }

    }

    private suspend fun update(exerciseToUpdate: Exercise) {
        withContext(Dispatchers.IO) {
            database.update(exerciseToUpdate)
        }
    }
    private fun onUserInputNothing() {
        _userInputNothingForExerciseNameEvent.value = true
    }
    fun finishedNotifyingUserInputtedNothing() {
        _userInputNothingForExerciseNameEvent.value = false
    }
    fun finishedExitingDialog() {
        _exitDialogEventOnActionComplete.value = false
    }

}