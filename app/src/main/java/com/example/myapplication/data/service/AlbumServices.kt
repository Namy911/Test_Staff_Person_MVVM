package com.example.myapplication.data.service

import com.example.myapplication.data.entity.Album
import com.example.myapplication.data.entity.Photo
import com.example.myapplication.data.entity.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AlbumServices {

    @GET("albums")
    suspend fun getAlbums(): Response<List<Album>>

    @GET("users")
    suspend fun getUsers(): Response<List<User>>

    @GET("photos")
    suspend fun getAlbumById(
        @Query("albumId") albumId: Int
    ): Response<List<Photo>>
}