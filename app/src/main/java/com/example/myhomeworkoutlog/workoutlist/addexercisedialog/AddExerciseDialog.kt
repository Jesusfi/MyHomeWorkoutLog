package com.example.myhomeworkoutlog.workoutlist.addexercisedialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
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

class AddExerciseDialog : DialogFragment() {

    lateinit var binding: FragmentAddExerciseDialogBinding
    lateinit var viewModel: AddExerciseViewModel
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)

        //Bind
        binding = FragmentAddExerciseDialogBinding.inflate(LayoutInflater.from(context))
        binding.lifecycleOwner = this

        //Set viewModel
        val application = requireNotNull(this.activity).application
        val dataSource = WorkoutLoggerDatabase.getInstance(application).exerciseDao
        val viewModelFactory = AddExerciseViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AddExerciseViewModel::class.java)
        binding.viewModel = viewModel

        //Set adapter
        val spinnerAdapter = ArrayAdapter.createFromResource(application.applicationContext, R.array.exercise_type, R.layout.spinner_item)
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.spinner.adapter = spinnerAdapter

        setOnClickListeners()

        viewModel.userInputNothingForExerciseNameEvent.observe(this, Observer {
            if(it){
                notifyUserToEnterExerciseName()
            }
        })


        builder.setView(binding.root)
        return builder.create()
    }

    private fun notifyUserToEnterExerciseName() {
        binding.editTextExerciseName.error = getString(R.string.error_enter_exercise_name)
        viewModel.finishedNotifyingUserInputtedNothing()
    }

    private fun setOnClickListeners() {
        binding.buttonAddExercise.setOnClickListener {
            val exerciseName = binding.editTextExerciseName.text.toString()
            val exerciseType = binding.spinner.selectedItem.toString()

            if(TextUtils.isEmpty(exerciseName)){
                viewModel.onUserInputNothing()
            }else{
                viewModel.onCreateNewExercise(exerciseName, exerciseType)
                dismiss()
            }

        }
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