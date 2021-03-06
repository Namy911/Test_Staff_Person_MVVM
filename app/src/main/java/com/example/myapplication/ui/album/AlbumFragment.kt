package com.example.myapplication.ui.album

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.entity.Album
import com.example.myapplication.databinding.AlbumFragmentBinding
import com.example.myapplication.databinding.AlbumItemRowBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "AlbumFragment"
@AndroidEntryPoint
class AlbumFragment : Fragment() {

    private val viewModel: AlbumViewModel by viewModels()
    private lateinit var binding: AlbumFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = AlbumFragmentBinding.inflate(layoutInflater).apply { binding = this }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AlbumAdapter(){
            findNavController().navigate(AlbumFragmentDirections.DisplayAlbumFragment(it))
        }
        binding.apply {
            albumContainer.adapter = adapter
        }
        viewModel.album.observe(viewLifecycleOwner){
            adapter.submitList(it.toMutableList())
        }
    }

    inner class AlbumAdapter(private val action: (Album) -> Unit): ListAdapter<Album, AlbumViewHolder>(diff){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder =
            AlbumViewHolder(AlbumItemRowBinding.inflate(layoutInflater, parent, false), action)

        override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
            holder.bind(getItem(position))
        }

    }

    inner class AlbumViewHolder(
        private val binding: AlbumItemRowBinding,
        val action: (Album) -> Unit): RecyclerView.ViewHolder(binding.root){

        init {
            itemView.setOnClickListener { }
        }

        fun bind(model: Album){
            binding.album = model
            binding.holder = this
            binding.executePendingBindings()
        }
    }

    internal val diff = object: DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem == newItem
        }
    }
}