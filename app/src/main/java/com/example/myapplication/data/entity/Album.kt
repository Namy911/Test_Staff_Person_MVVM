package com.example.myapplication.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Album(
    val id: Int,
    val title: String,
    val userId: Int
) : Parcelable