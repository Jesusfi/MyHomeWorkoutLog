package com.example.myhomeworkoutlog.fragments.exerciselist.contextmenudialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myhomeworkoutlog.R
import com.example.myhomeworkoutlog.database.WorkoutLoggerDatabase
import com.example.myhomeworkoutlog.databinding.FragmentContextMenuDialogBinding
import com.example.myhomeworkoutlog.fragments.exerciselist.updateexercisedialog.UpdateExerciseDialog

class ContextMenuListDialog private constructor() : DialogFragment() {

    lateinit var binding: FragmentContextMenuDialogBinding
    lateinit var viewModel: ContextMenuListViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //Get arguments
        val exerciseId = arguments?.getLong(BUNDLE_KEY)
        Log.d("bundleTest", "Got key ${exerciseId}")

        //inflate view
        binding = FragmentContextMenuDialogBinding.inflate(LayoutInflater.from(context))
        binding.lifecycleOwner = this

        val application = requireNotNull(this.activity).application
        val dataSource = WorkoutLoggerDatabase.getInstance(application).exerciseDao
        val viewModelFactory =
            ContextMenuListViewModelFactory(
                dataSource,
                application
            )
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(ContextMenuListViewModel::class.java)

        binding.viewModel = viewModel
        binding.exerciseId = exerciseId

        viewModel.exitDialogOnDeleteComplete.observe(this, Observer {
            if (it) {
                dismiss()
                Log.d("ContextMenuDialog", "Reached dismiss")
                viewModel.finishedExitDialog()
            }
        })

        viewModel.navigateToEditExerciseDialog.observe(this, Observer {
            if (it) {
                navigateToUpdateExercise(exerciseId)
                viewModel.onNavigationToEditDialogComplete()
            }
        })

        val builder = AlertDialog.Builder(activity)
        builder.setView(binding.root)

        return builder.create()
    }

    private fun navigateToUpdateExercise(exerciseId: Long?) {
        exerciseId?.let {
            showUpdateExerciseDialog(exerciseId)
        }
    }

    private fun showUpdateExerciseDialog(exerciseId: Long) {
        val ft = parentFragmentManager.beginTransaction()
        val prev = parentFragmentManager.findFragmentByTag(UpdateExerciseDialog.TAG)

        if (prev != null) {
            ft.remove(prev).commit()
        }
        val dialogFragment = UpdateExerciseDialog.newInstance(exerciseId)
        parentFragmentManager.popBackStackImmediate()
        ft.addToBackStack(null)
        dialogFragment.show(ft, UpdateExerciseDialog.TAG)
    }

    companion object {
        const val TAG = "ContextMenuExerciseListDialog"
        private const val BUNDLE_KEY = "exerciseKey"

        fun getInstance(exerciseId: Long): ContextMenuListDialog {
            val dialog =
                ContextMenuListDialog()
            val bundle = Bundle()

            bundle.putLong(BUNDLE_KEY, exerciseId)
            dialog.arguments = bundle
            return dialog
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