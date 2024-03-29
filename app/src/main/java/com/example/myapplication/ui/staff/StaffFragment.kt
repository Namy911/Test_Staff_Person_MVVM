package com.example.myapplication.ui.staff

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.example.myapplication.data.model.StaffAndPersons
import com.example.myapplication.databinding.ListStaffFragmentBinding
import com.example.myapplication.databinding.StaffItemRowBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

private const val TAG = "StaffFragment"
@AndroidEntryPoint
class StaffFragment : Fragment() {


    lateinit var binding: ListStaffFragmentBinding
    private val viewModel: StaffViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        ListStaffFragmentBinding.inflate(layoutInflater, container, false)
            .apply { binding = this }
            .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = StaffAdapter()
        lifecycleScope.launchWhenStarted {
            viewModel.listStaff.collect {
                binding.apply {
                    listStaff.adapter = adapter
                    listStaff.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
                    adapter.submitList(it)
                }
            }
        }
    }
    inner class StaffAdapter(): ListAdapter<StaffAndPersons, StaffViewHolder>(diff){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                StaffViewHolder(StaffItemRowBinding.inflate(layoutInflater, parent, false))

        override fun onBindViewHolder(holder: StaffViewHolder, position: Int) {
            holder.bind(getItem(position))
        }
    }

    internal val diff = object: DiffUtil.ItemCallback<StaffAndPersons>(){
        override fun areItemsTheSame(oldItem: StaffAndPersons, newItem: StaffAndPersons): Boolean {
            return oldItem.staff.id == newItem.staff.id
        }

        override fun areContentsTheSame(oldItem: StaffAndPersons, newItem: StaffAndPersons): Boolean {
            return oldItem == newItem
        }

    }

    inner class StaffViewHolder(private val binding: StaffItemRowBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(model: StaffAndPersons){
            binding.model = model
            itemView.setOnClickListener {
                findNavController().navigate(StaffFragmentDirections.displayStaffDetailsFragment(model))
            }
            binding.executePendingBindings()
        }
    }
}