package com.example.myapplication.ui.staff

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.StaffAndPersons
import com.example.myapplication.repository.StaffAndPersonsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StaffViewModel @Inject constructor(
    private val repository: StaffAndPersonsRepository
): ViewModel() {
    private val _listStaff = MutableSharedFlow<List<StaffAndPersons>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val listStaff: SharedFlow<List<StaffAndPersons>> = _listStaff.asSharedFlow()

    init {
        getAll()
    }

    private fun getAll(){
        viewModelScope.launch{ _listStaff.emitAll(repository.loadAllStaff()) }
    }
}