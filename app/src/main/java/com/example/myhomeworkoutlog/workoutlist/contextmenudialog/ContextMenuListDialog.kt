package com.example.myhomeworkoutlog.workoutlist.contextmenudialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myhomeworkoutlog.database.WorkoutLoggerDatabase
import com.example.myhomeworkoutlog.databinding.FragmentContextMenuDialogBinding
import com.example.myhomeworkoutlog.workoutlist.ExerciseListViewModel
import com.example.myhomeworkoutlog.workoutlist.ExerciseListViewModelFactory

class ContextMenuListDialog private constructor(): DialogFragment() {

    lateinit var binding: FragmentContextMenuDialogBinding
    lateinit var viewModel: ContextMenuListViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //Get arguments
        val exerciseId = arguments?.getLong(BUNDLE_KEY)
        Log.d("bundleTest","Got key ${exerciseId}")
        //inflate view
        binding = FragmentContextMenuDialogBinding.inflate(LayoutInflater.from(context))
        binding.lifecycleOwner = this


        val application = requireNotNull(this.activity).application
        val dataSource = WorkoutLoggerDatabase.getInstance(application).exerciseDao
        val viewModelFactory = ContextMenuListViewModelFactory(dataSource, application)

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(ContextMenuListViewModel::class.java)

        binding.viewModel = viewModel
        binding.exerciseId = exerciseId

       viewModel.exitDialogEventOnActionComplete.observe(this, Observer {
           if(it){
               dismiss()
               viewModel.finishedExitDialog()
           }
       })

        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Choose Action")
        builder.setView(binding.root)

        return builder.create()
    }

    companion object{
        const val TAG = "ContextMenuExerciseListDialog"
        private const val BUNDLE_KEY = "exerciseKey"

        fun getInstance(exerciseId: Long): ContextMenuListDialog {
            val dialog = ContextMenuListDialog()
            val bundle = Bundle()

            bundle.putLong(BUNDLE_KEY, exerciseId)
            dialog.arguments = bundle
            return dialog
        }
    }
}