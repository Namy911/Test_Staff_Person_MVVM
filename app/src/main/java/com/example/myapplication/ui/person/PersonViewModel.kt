package com.example.myapplication.ui.person

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.myapplication.data.entity.Person
import com.example.myapplication.data.model.PersonAndStaff
import com.example.myapplication.data.entity.Staff
import com.example.myapplication.di.ApplicationScope
import com.example.myapplication.repository.PersonRepository
import com.example.myapplication.repository.StaffAndPersonsRepository
import com.example.myapplication.repository.StaffRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
    private val repoPerson: PersonRepository,
    private val repoStuff: StaffRepository,
    private val repoStuffAndPersons: StaffAndPersonsRepository,
    private val savedStateHandle: SavedStateHandle,
    @ApplicationScope private val appScope: CoroutineScope
) : ViewModel() {
    companion object {
        const val SPINNER_SELECTION: String = "ui.list.spinner_selection"
        const val PERSON_STATE: String = "ui.list.person.state"
        const val PERSON_PIKER_STATE: String = "ui.list.person.piker_state"
    }

    private val _allPerson = MutableSharedFlow<List<Person>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val allPerson: SharedFlow<List<Person>> = _allPerson.asSharedFlow()

    private val _personAndStaff = MutableSharedFlow<PersonAndStaff>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val personAndStaff: SharedFlow<PersonAndStaff> = _personAndStaff

    private val _allStaff = MutableSharedFlow<List<Staff>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val allStaff: SharedFlow<List<Staff>> = _allStaff.asSharedFlow()


    val listPersons = Pager(PagingConfig(pageSize = 10)) {
        repoPerson.allPerson()
    }.flow.cachedIn(viewModelScope)

// ====================================== Save State Work ============================================= //

    fun saveSpinnerSelection(model: Staff){
        savedStateHandle.set(SPINNER_SELECTION, model)
    }

    fun getSpinnerSelection(): Staff? = savedStateHandle.get<Staff>(SPINNER_SELECTION)

    fun deletePerson(model: Person){
        appScope.launch{ repoPerson.deletePerson(model) }
    }

    fun savePersonState(model: Person){
        val stamp = getPikerPersonState()
        stamp?.let {
            model.copy(date = Date(it))
            savedStateHandle.set(PERSON_STATE, model)
        } ?: savedStateHandle.set(PERSON_STATE, model)
    }

    fun getPersonState(): Person? = savedStateHandle.get<Person>(PERSON_STATE)

    fun savePikerPersonState(stamp: Long) = savedStateHandle.set(PERSON_PIKER_STATE, stamp)
    fun getPikerPersonState(): Long? = savedStateHandle.get<Long>(PERSON_PIKER_STATE)

// ======================================== DB Work ============================================= //

    fun insertPerson(model: Person){
        viewModelScope.launch(IO) { repoPerson.insertPerson(model) }
    }

    fun getAllStaff() {
        viewModelScope.launch { _allStaff.emitAll(repoStuff.getAllStaff()) }
    }
    // Display Person with  staff info
    fun getPersonAndStaff(id: Int){
        viewModelScope.launch {
//            _personAndStaff.emitAll(
//                flow { emit(repoStuffAndPersons.getPersonAndStaff(id)) }
//            )
            _personAndStaff.emit(repoStuffAndPersons.getPersonAndStaff(id))
        }
    }

//    private fun allPerson(){
//        viewModelScope.launch {
//            _allPerson.emitAll(repoPerson.allPeron())
//        }
//    }
}