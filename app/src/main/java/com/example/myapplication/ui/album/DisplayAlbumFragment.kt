package com.example.myapplication.ui.album

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.entity.Person
import com.example.myapplication.data.entity.Photo
import com.example.myapplication.databinding.DisplayAlbumFragmentBinding
import com.example.myapplication.databinding.DisplayFragmentBinding
import com.example.myapplication.databinding.PhotoItemRowBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "DisplayAlbumFragment"

@AndroidEntryPoint
class DisplayAlbumFragment : Fragment() {

    private val viewModel: AlbumViewModel by viewModels()
    private val args: DisplayAlbumFragmentArgs by navArgs()
    private lateinit var binding: DisplayAlbumFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DisplayAlbumFragmentBinding.inflate(layoutInflater, container, false).apply { binding = this }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = args.albumId.id
        viewModel.getAlbumById(id)
        // Setup list Adapter
        val adapter = PhotoAdapter()
        binding.containerPhoto.adapter = adapter
        viewModel.photo.observe(viewLifecycleOwner){ adapter.submitList(it.toMutableList()) }
    }

    inner class PhotoAdapter: ListAdapter<Photo, PhotoViewHolder>(diff){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
            return PhotoViewHolder(PhotoItemRowBinding.inflate(layoutInflater, parent, false))
        }

        override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
            holder.bind(getItem(position))
        }

    }

    inner class PhotoViewHolder(val binding: PhotoItemRowBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(model:Photo){
            binding.txtPhotoTitle.text = model.title
            Glide.with(requireActivity())
                .load(model.url) // Bug: 410 file not find
                .centerCrop()
                .placeholder(R.drawable.person_1)
                .into(binding.imgPhoto)
        }

    }

    internal val diff = object: DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }
}