package com.example.myapplication.ui.person

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.*
import com.example.myapplication.R
import com.example.myapplication.data.entity.Person
import com.example.myapplication.databinding.PersonItemRowBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

private const val TAG = "ListFragment"
@AndroidEntryPoint
class ListFragment : Fragment(R.layout.list_fragment) {

    private val viewModel: PersonViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = view.findViewById<RecyclerView>(R.id.task_list)
        val fabAddPerson = view.findViewById<FloatingActionButton>(R.id.fab_add_person)
        val fabListStaff = view.findViewById<FloatingActionButton>(R.id.fb_list_staff)
        val fabAlbum = view.findViewById<FloatingActionButton>(R.id.fab_album)

        fabListStaff.setOnClickListener { findNavController().navigate(ListFragmentDirections.staffFragment()) }
        fabAlbum.setOnClickListener { findNavController().navigate(ListFragmentDirections.userFragment()) }
        fabAddPerson.setOnClickListener { findNavController().navigate(ListFragmentDirections.addPersonFragment(null)) }
        //Setup List Adapter
        val adapter = PersonAdapter()
        list.adapter = adapter
        lifecycleScope.launchWhenStarted {
            viewModel.listPersons.collectLatest {
                adapter.submitData(it)
            }
        }
        // Setup swipe delete
        val swipeHandler = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                (viewHolder as PersonViewHolder).person?.let { person ->
                    viewModel.deletePerson(person)
                    Snackbar.make(
                        requireView(),
                        getString(R.string.snack_delete_item, person.name),
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(getString(R.string.snack_btn_message)) {
                            viewModel.insertPerson(person)
                        }
                        .setActionTextColor(Color.GREEN)
                        .show()
                }
            }
        }
        ItemTouchHelper(swipeHandler).apply {
            attachToRecyclerView(list)
        }
    }

    inner class PersonAdapter : PagingDataAdapter<Person, PersonViewHolder>(diff) {
        override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
            val person: Person? = getItem(position)
            holder.bind(person)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
            return PersonViewHolder(PersonItemRowBinding.inflate(layoutInflater, parent, false))
        }
    }

    inner class PersonViewHolder(private val binding: PersonItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        var person: Person? = null
            private set

        fun bind(model: Person?){
            binding.apply {
                model?.let {
                    txtSurName.text = model.surName
                    txtName.text = model.name
                    imgPerson.setOnClickListener {
                        findNavController().navigate(ListFragmentDirections.displayPersonFragment(model.id))
                    }
                    person = model
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