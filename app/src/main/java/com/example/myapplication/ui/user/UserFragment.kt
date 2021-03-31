package com.example.myapplication.ui.user

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.entity.Person
import com.example.myapplication.data.entity.User
import com.example.myapplication.databinding.UserFragmentBinding
import com.example.myapplication.databinding.UserItemRowBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {

    private val viewModel: UserViewModel by viewModels()

    private var _binding: UserFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: UserPaginingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UserFragmentBinding.inflate(inflater, container, false )

        adapter = UserPaginingAdapter()
        binding.apply {
            albumContainer.adapter = adapter
        }
        return  binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class UserPaginingAdapter: PagingDataAdapter<User, UserViewHolder>(diff){
        override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
            val user: User? = getItem(position)
            holder.bind(user)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
            return UserViewHolder(UserItemRowBinding.inflate(layoutInflater, parent, false))
        }
    }

    inner class UserViewHolder(private val binding: UserItemRowBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(model: User?){
            binding.apply {
                model?.let {
                    albumTitle.text = model.data[0].first_name
                }
            }
        }
    }

    internal val diff = object: DiffUtil.ItemCallback<User>(){
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.data[0].id == newItem.data[0].id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

    }
}