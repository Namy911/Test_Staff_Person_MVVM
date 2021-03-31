package com.example.myapplication.data.entity

import androidx.paging.PagingSource
import androidx.room.*

@Entity(tableName = "users")
data class Data(
    val avatar: String,
    val email: String,
    val first_name: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val last_name: String
){
    @Dao
    interface Store {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertAllUsers(users: List<Data>)

        @Query("SELECT * FROM `users`")
        fun getAllUsers(): PagingSource<Int, User>
    }
}