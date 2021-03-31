package com.example.myapplication.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.myapplication.data.entity.Data
import com.example.myapplication.data.entity.User
import com.example.myapplication.data.service.UserServices
import java.io.IOException
import javax.inject.Inject

class UserRepository @Inject constructor(private val service: UserServices, private val userStore: Data.Store) {

    suspend fun getListUsers() = service.getListUsers()
    fun getAllUsers() = userStore.getAllUsers()
    suspend fun insertUser(users: List<Data>) = userStore.insertAllUsers(users)


}

@ExperimentalPagingApi
class UserPageRemoteMediator(
    private val networkService: UserServices,
    private val repository: UserRepository
) : RemoteMediator<Int, User>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, User>
    ): MediatorResult {
        return try {
            val response = networkService.getListUsers()
            val users = response.data
            repository.insertUser(users)

            val endOfPaginationReached = users.isEmpty()
            return MediatorResult.Success(
                endOfPaginationReached = endOfPaginationReached
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        }
    }
}