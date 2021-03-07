package com.example.myapplication.di

import com.example.myapplication.data.service.AlbumServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {
    const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    @Singleton
    @Provides
    fun provideAlbumServices(retrofit: Retrofit): AlbumServices =
        retrofit.create(AlbumServices::class.java)

    @Singleton
    @Provides
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun logging() = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    @Singleton
    @Provides
    fun client() = OkHttpClient.Builder()
        .addInterceptor(logging())
        .build()
}