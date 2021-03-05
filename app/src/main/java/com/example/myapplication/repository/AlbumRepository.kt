package com.example.myapplication.repository

import com.example.myapplication.data.service.AlbumServices
import javax.inject.Inject

class AlbumRepository  @Inject constructor(private val service: AlbumServices) {

    suspend fun getAlbums() = service.getAlbums()
}