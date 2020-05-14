package com.example.myhomeworkoutlog.fragments.mainworkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myhomeworkoutlog.R
import com.example.myhomeworkoutlog.databinding.FragmentMainBinding

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(layoutInflater)

        // Specify the current fragment as the lifecycle owner.
        binding.lifecycleOwner = this

        val viewModel: MainFragmentViewModel = ViewModelProvider(this).get(
            MainFragmentViewModel::class.java)
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
