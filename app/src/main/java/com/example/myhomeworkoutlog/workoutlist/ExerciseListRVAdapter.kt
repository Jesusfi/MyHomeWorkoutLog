package com.example.myhomeworkoutlog.workoutlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.example.myhomeworkoutlog.database.Exercise
import com.example.myhomeworkoutlog.databinding.ListItemExerciseBinding

class ExerciseListRVAdapter(
    private val exerciseListener: ExerciseListener,
    private val longClickListener: ((exerciseId: Long) -> Unit)
) :
    ListAdapter<Exercise, ExerciseListRVAdapter.ViewHolder>(ExerciseDiffCallback()) {


    class ViewHolder private constructor(private val binding: ListItemExerciseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: Exercise,
            exerciseListener: ExerciseListener,
            longClickLister: ((exerciseId: Long) -> Unit)
        ) {
            binding.exercise = item
            binding.clickListener = exerciseListener
            binding.executePendingBindings()

            binding.parent.setOnLongClickListener {
                longClickLister(item.exerciseId)
                true
            }

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemExerciseBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, exerciseListener, longClickListener)
    }

    class ExerciseDiffCallback() : DiffUtil.ItemCallback<Exercise>() {
        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.exerciseId == newItem.exerciseId
        }

        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem == newItem
        }

    }

    class ExerciseListener(val clickListener: (exerciseId: Long) -> Unit) {
        fun onClick(exercise: Exercise) = clickListener(exercise.exerciseId)
    }


}