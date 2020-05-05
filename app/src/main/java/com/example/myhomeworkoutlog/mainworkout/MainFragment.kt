package com.example.myhomeworkoutlog.mainworkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.myhomeworkoutlog.R
import com.example.myhomeworkoutlog.databinding.FragmentMainBinding

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        view.findViewById<Toolbar>(R.id.toolbar)
            .setupWithNavController(navController, appBarConfiguration)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(layoutInflater)

        // Specify the current fragment as the lifecycle owner.
        binding.lifecycleOwner = this

        val viewModel: MainFragmentViewModel = ViewModelProvider(this).get(MainFragmentViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.navigateToWorkoutListEvent.observe(viewLifecycleOwner, Observer {
            if (it == true){
                findNavController().navigate(R.id.action_mainFragment_to_workoutListFragment)
                viewModel.navigateToWorkoutListEventComplete()
            }
        })

        return binding.root
    }

}
