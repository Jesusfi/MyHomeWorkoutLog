package com.example.myhomeworkoutlog.fragments.exerciselist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myhomeworkoutlog.database.Exercise
import com.example.myhomeworkoutlog.database.ExerciseDao
import kotlinx.coroutines.*

class ExerciseListViewModel(
    private val database: ExerciseDao,
    applicationContext: Application
) : AndroidViewModel(applicationContext) {

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

    val allExercises = database.getAllExercises()

    /**
     * Called when the ViewModel is dismantled.
     * At this point, we want to cancel all coroutines;
     * otherwise we end up with processes that have nowhere to return to
     * using memory and resources.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    private val _createNewWorkoutEvent = MutableLiveData<Boolean>()
    public val createNewWorkoutEvent: LiveData<Boolean>
        get() = _createNewWorkoutEvent


    init {
        _createNewWorkoutEvent.value = false
    }

    fun onAddNewExerciseToList(exercise: Exercise){
        uiScope.launch {
            val exerciseTest = Exercise(exerciseName = "pushups", exerciseType = "Cardio")
            insert(exerciseTest)

        }
    }
    private suspend fun insert(exercise: Exercise){
        withContext(Dispatchers.IO){
            database.insert(exercise)
        }
    }

    fun onClear(){
        uiScope.launch {
            clear()
        }
    }

    fun onDeleteExerciseById(exerciseId: Long){
        uiScope.launch {
            deleteExerciseWithId(exerciseId)
        }
    }

    private suspend fun deleteExerciseWithId(exerciseId: Long) {
        withContext(Dispatchers.IO){
            database.deleteByExerciseId(exerciseId)
        }
    }

    private suspend fun clear(){
        withContext(Dispatchers.IO){
            database.clear()
        }
    }
    fun userClickedCreateNewWorkoutButton() {
        _createNewWorkoutEvent.value = true
    }

    fun finishedCreatingNewWorkoutEvent() {
        _createNewWorkoutEvent.value = false
    }
}