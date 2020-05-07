package com.example.myhomeworkoutlog.exerciselist.updateexercisedialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myhomeworkoutlog.R
import com.example.myhomeworkoutlog.database.WorkoutLoggerDatabase
import com.example.myhomeworkoutlog.databinding.FragmentAddExerciseDialogBinding
import com.example.myhomeworkoutlog.databinding.FragmentUpdateExerciseDialogBinding
import com.example.myhomeworkoutlog.exerciselist.addexercisedialog.AddExerciseDialog
import com.example.myhomeworkoutlog.exerciselist.addexercisedialog.AddExerciseViewModel
import com.example.myhomeworkoutlog.exerciselist.addexercisedialog.AddExerciseViewModelFactory

class UpdateExerciseDialog private constructor() : DialogFragment() {
    lateinit var binding: FragmentUpdateExerciseDialogBinding
    lateinit var viewModel: UpdateExerciseViewModel

    companion object {
        const val TAG = "UpdateExerciseDialog"
        const val EXERCISE_ID_KEY = "exerciseIdKey"

        fun newInstance(exerciseId: Long): UpdateExerciseDialog {
            val dialog = UpdateExerciseDialog()
            val bundle = Bundle()

            bundle.putLong(EXERCISE_ID_KEY, exerciseId)
            dialog.arguments = bundle

            return dialog
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)

        val exerciseId = arguments?.getLong(EXERCISE_ID_KEY)

        //Bind
        binding = FragmentUpdateExerciseDialogBinding.inflate(LayoutInflater.from(context))
        binding.lifecycleOwner = this

        //Set viewModel
        val application = requireNotNull(this.activity).application
        val dataSource = WorkoutLoggerDatabase.getInstance(application).exerciseDao
        val viewModelFactory = UpdateExerciseViewModelFactory(dataSource, application, exerciseId!!)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(UpdateExerciseViewModel::class.java)
        binding.viewModel = viewModel

        //Set adapter
        val spinnerAdapter = ArrayAdapter.createFromResource(
            application.applicationContext,
            R.array.exercise_type,
            R.layout.spinner_item
        )
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.spinner.adapter = spinnerAdapter

        //Listeners
        binding.buttonUpdate.setOnClickListener {

            val exerciseToUpdate = binding.exercise

            val exerciseName = binding.editTextExerciseName.text.toString()
            val exerciseType = binding.spinner.selectedItem.toString()

            if (exerciseToUpdate != null) {
                viewModel.onUpdateExercise(exerciseToUpdate, exerciseName, exerciseType)
            }
        }

        // Observers
        viewModel.exerciseToUpdate.observe(this, Observer {
            binding.exercise = it
            setSpinnerSelection(it.exerciseType)
        })
        viewModel.userInputNothingForExerciseNameEvent.observe(this, Observer {
            if (it) {
                notifyUserToEnterExerciseName()
            }
        })
        viewModel.exitDialogEventOnActionComplete.observe(this, Observer {
            if (it) {
                dismiss()
                viewModel.finishedExitingDialog()
            }
        })

        builder.setView(binding.root)
        return builder.create()
    }

    private fun setSpinnerSelection(exerciseType: String) {
        var itemPosition = 0
        if (exerciseType.equals("Cardio")) {
            itemPosition = 0
        } else if (exerciseType.equals("Strength")) {
            itemPosition = 1
        }
        binding.spinner.setSelection(itemPosition)
    }

    private fun notifyUserToEnterExerciseName() {
        binding.editTextExerciseName.error = getString(R.string.error_enter_exercise_name)
        viewModel.finishedNotifyingUserInputtedNothing()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_rounded)
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}