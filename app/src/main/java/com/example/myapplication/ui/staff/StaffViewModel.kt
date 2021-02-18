package com.example.myapplication.ui.staff

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.StaffAndPersons
import com.example.myapplication.repository.StaffAndPersonsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StaffViewModel @Inject constructor(
    private val repository: StaffAndPersonsRepository
): ViewModel() {
    private val _listStaff: MutableLiveData<List<StaffAndPersons>> by  lazy {MutableLiveData<List<StaffAndPersons>>() }
    val listStaff: LiveData<List<StaffAndPersons>> = _listStaff

    private val _test2: MutableLiveData<StaffAndPersons> by  lazy {MutableLiveData<StaffAndPersons>() }
    val personAndStuff: LiveData<StaffAndPersons> = _test2

    init {
        getAll()
    }

    private fun getAll(){
        viewModelScope.launch{
            repository.loadAllStaff().collect {
                _listStaff.value = it
            }
        }
    }

    fun getStuff(id: Int){
        viewModelScope.launch {
            repository.getPersonsByStaff(id).collect {
                _test2.value = it
            }
        }
    }
}