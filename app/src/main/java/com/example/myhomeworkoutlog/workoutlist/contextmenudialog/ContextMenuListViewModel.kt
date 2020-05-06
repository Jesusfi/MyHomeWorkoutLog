package com.example.myhomeworkoutlog.workoutlist.contextmenudialog

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myhomeworkoutlog.database.ExerciseDao
import kotlinx.coroutines.*

class ContextMenuListViewModel(
    val database: ExerciseDao,
    application: Application
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

    private val _exitDialogEventOnActionComplete = MutableLiveData<Boolean>()
    val exitDialogEventOnActionComplete: LiveData<Boolean>
        get() = _exitDialogEventOnActionComplete

    init {
        _exitDialogEventOnActionComplete.value = false
    }

    fun onDeleteSelectedExercise(exerciseId: Long) {
        uiScope.launch {
            Log.d("deleteItem", "in ui scope launch before")
            deleteExercise(exerciseId)
            _exitDialogEventOnActionComplete.value = true
            Log.d("deleteItem", "in ui scope launch after")
        }
    }

    private suspend fun deleteExercise(exerciseId: Long) {
        withContext(Dispatchers.IO) {
            database.deleteByExerciseId(exerciseId)
            Log.d("deleteItem", "in deleteExercise")
        }
    }
    fun finishedExitDialog(){
        _exitDialogEventOnActionComplete.value = false
        Log.d("deleteItem", "exit complete")

    }
}