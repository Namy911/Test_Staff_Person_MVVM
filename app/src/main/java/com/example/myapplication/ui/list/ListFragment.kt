package com.example.myapplication.ui.list

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.entity.Person
import com.example.myapplication.databinding.PersonItemBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "ListFragment"
@AndroidEntryPoint
class ListFragment : Fragment(R.layout.list_fragment) {

    private val viewModel: ListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = view.findViewById<RecyclerView>(R.id.task_list)
        val fabAddPerson = view.findViewById<FloatingActionButton>(R.id.fab_add_person)
        val fabListStaff = view.findViewById<FloatingActionButton>(R.id.fb_test)

        fabListStaff.setOnClickListener { findNavController().navigate(ListFragmentDirections.staffFragment()) }
        fabAddPerson.setOnClickListener { findNavController().navigate(ListFragmentDirections.addPersonFragment(null)) }
        //Setup List Adapter
        val adapter = TaskAdapter()
        list.adapter = adapter
        viewModel.allPerson.observe(viewLifecycleOwner){ adapter.submitList(it.toMutableList()) }
        // Setup swipe delete
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val person = adapter.currentList[position]

                viewModel.deletePerson(person)
                Snackbar.make(requireView(), getString(R.string.snack_delete_item, person.name), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.snack_btn_message)) {
                            viewModel.insertPerson(person)
                            list.scrollToPosition(position)
                        }
                        .setActionTextColor(Color.GREEN)
                        .show()
            }
        }
        ItemTouchHelper(swipeHandler).apply {
            attachToRecyclerView(list)
        }
    }

    inner class TaskAdapter : ListAdapter<Person, TaskViewHolder>(diff) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
            return TaskViewHolder(PersonItemBinding.inflate(layoutInflater, parent, false))
        }

        override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
            holder.bind(getItem(position))
        }
    }

    inner class TaskViewHolder(private val binding: PersonItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: Person){
            binding.apply {
                txtSurName.text = model.surName
                txtName.text = model.name
                imgPerson.setOnClickListener {
                    findNavController().navigate(ListFragmentDirections.displayPersonFragment(model.id))
                }
            }
        }
    }

    internal val diff = object: DiffUtil.ItemCallback<Person>() {
        override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem == newItem
        }
    }
}