package com.example.myapplication.data.service

import com.example.myapplication.data.entity.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserServices {

    @GET("api/users")
    suspend fun getListUsers(): User


//    @GET("photos")
//    suspend fun getAlbumById(
//        @Query("albumId") albumId: Int
//    ): Response<List<Photo>>
}