package com.example.myapplication.data.service

import com.example.myapplication.data.entity.Album
import retrofit2.Response
import retrofit2.http.GET

interface AlbumServices {

    @GET("albums")
    suspend fun getAlbums(): Response<List<Album>>
}