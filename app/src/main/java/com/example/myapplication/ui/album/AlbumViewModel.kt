package com.example.myapplication.ui.album

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.entity.Album
import com.example.myapplication.repository.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AlbumViewModel @Inject constructor( private val repo: AlbumRepository): ViewModel() {

    private val _albums: MutableLiveData<List<Album>> by lazy { MutableLiveData<List<Album>>() }
    val album get() = _albums

    init {
        getAlbums()
    }

    fun getAlbums(){
        viewModelScope.launch {
            if (repo.getAlbums().isSuccessful) {
                repo.getAlbums().body()?.let { result ->
                    _albums.value = result
                }
            }
        }
    }
}