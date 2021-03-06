package com.example.myapplication.ui.album

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.entity.Album
import com.example.myapplication.data.entity.Photo
import com.example.myapplication.data.entity.User
import com.example.myapplication.repository.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AlbumViewModel @Inject constructor( private val repo: AlbumRepository): ViewModel() {

    private val _albums: MutableLiveData<List<Album>> by lazy { MutableLiveData<List<Album>>() }
    val album get() = _albums

    private val _users: MutableLiveData<List<User>> by lazy { MutableLiveData<List<User>>() }
    val user get() = _users

    private val _photos: MutableLiveData<List<Photo>> by lazy { MutableLiveData<List<Photo>>() }
    val photo get() = _photos


    init {
        getAlbums()
        getUsers()
    }

    private fun getAlbums(){
        viewModelScope.launch {
            val response = repo.getAlbums()
            if (response.isSuccessful) {
                response.body()?.let { result ->
                    _albums.value = result
                }
            }
        }
    }

    private fun getUsers(){
        viewModelScope.launch {
            val response = repo.getUsers()
            if (response.isSuccessful){
                response.body()?.let {
                    _users.value = it
                }
            }
        }
    }

    fun getAlbumById(id: Int = 3){
        viewModelScope.launch {
            val response = repo.getAlbumById(id)
            if (response.isSuccessful){
                response.body()?.let {
                    _photos.value = it
                }
            }
        }
    }
}