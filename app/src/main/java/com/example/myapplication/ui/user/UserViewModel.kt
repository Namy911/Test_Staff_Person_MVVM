package com.example.myapplication.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.myapplication.data.entity.Person
import com.example.myapplication.data.entity.User
import com.example.myapplication.data.service.UserServices
import com.example.myapplication.repository.UserPageRemoteMediator
import com.example.myapplication.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    @Inject lateinit var userService: UserServices

    private val pagingSource = repository.getAllUsers()

    @ExperimentalPagingApi
    val userList = Pager(
        config = PagingConfig(
            pageSize = 10
        ),
        remoteMediator = UserPageRemoteMediator(userService, repository)
    ){
        pagingSource
    }
}