package com.example.myhomeworkoutlog.exerciselist

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myhomeworkoutlog.R
import com.example.myhomeworkoutlog.database.WorkoutLoggerDatabase
import com.example.myhomeworkoutlog.databinding.FragmentWorkoutListBinding
import com.example.myhomeworkoutlog.exerciselist.addexercisedialog.AddExerciseDialog
import com.example.myhomeworkoutlog.exerciselist.contextmenudialog.ContextMenuListDialog
import com.google.android.material.snackbar.Snackbar


/**
 * A simple [Fragment] subclass.
 */
class ExerciseListFragment : Fragment() {
    lateinit var binding: FragmentWorkoutListBinding
    lateinit var viewModel: ExerciseListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWorkoutListBinding.inflate(layoutInflater)

        // Specify the current fragment as the lifecycle owner.
        binding.lifecycleOwner = this

        //Set Action bar
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)

        val application = requireNotNull(this.activity).application
        val dataSource = WorkoutLoggerDatabase.getInstance(application).exerciseDao
        val viewModelFactory = ExerciseListViewModelFactory(dataSource, application)

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(ExerciseListViewModel::class.java)

        binding.viewModel = viewModel

        val adapter =
            ExerciseListRVAdapter(ExerciseListRVAdapter.ExerciseListener { exerciseId ->
                Snackbar.make(
                    binding.layoutExerciseListParent,
                    "you clicked $exerciseId",
                    Snackbar.LENGTH_SHORT
                ).show()
            }) { exerciseId ->
                //confirmDeleteExercise(exerciseId)
                showContextMenuForExercise(exerciseId)
            }

        val manager = GridLayoutManager(context, 2)// StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)//
        binding.exerciseList.adapter = adapter
        binding.exerciseList.layoutManager = manager

        viewModel.allExercises.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.createNewWorkoutEvent.observe(viewLifecycleOwner, Observer {
            if (it) {
                addNewWorkoutItem()
                viewModel.finishedCreatingNewWorkoutEvent()
            }
        })
        return binding.root
    }

    private fun confirmDeleteExercise(exerciseId: Long) {
        AlertDialog.Builder(context)
            .setTitle("Delete entry")
            .setMessage("Are you sure you want to delete this entry?")
            .setPositiveButton(
                android.R.string.yes
            ) { dialog, which ->
                viewModel.onDeleteExerciseById(exerciseId)
            }
            .setNegativeButton(android.R.string.no, null)
            .show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        view.findViewById<Toolbar>(R.id.toolbar)
            .setupWithNavController(navController, appBarConfiguration)

    }

    private fun addNewWorkoutItem() {
        val ft = parentFragmentManager.beginTransaction()
        val prev = parentFragmentManager.findFragmentByTag(AddExerciseDialog.TAG)

        if (prev != null) {
            ft.remove(prev).commit()
        }
        val dialogFragment = AddExerciseDialog.newInstance()
        ft.addToBackStack(null)
        dialogFragment.show(ft, AddExerciseDialog.TAG)
    }

    private fun showContextMenuForExercise(exerciseId: Long) {
        val ft = parentFragmentManager.beginTransaction()
        val prev = parentFragmentManager.findFragmentByTag(ContextMenuListDialog.TAG)

        if (prev != null) {
            ft.remove(prev).commit()
        }

        ft.addToBackStack(null)
        val contextMenuDialog = ContextMenuListDialog.getInstance(exerciseId)
        contextMenuDialog.show(ft, ContextMenuListDialog.TAG)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.workoutlist_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.clear_option -> {
                clearList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun clearList() {
        viewModel.onClear()
        Snackbar.make(
            binding.layoutExerciseListParent,
            getString(R.string.notify_list_cleared),
            Snackbar.LENGTH_SHORT
        ).show()
    }

}
