package com.example.myapplication.ui.person

import androidx.lifecycle.*
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
import kotlinx.coroutines.flow.collect
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

    private val _allPerson: MutableLiveData<List<Person>> by lazy { MutableLiveData<List<Person>>() }
    val allPerson: LiveData<List<Person>> = _allPerson

    private val _person: MutableLiveData<Person> by lazy { MutableLiveData<Person>() }
    val person: LiveData<Person> = _person

    private val _personAndStaff: MutableLiveData<PersonAndStaff> by lazy { MutableLiveData<PersonAndStaff>() }
    val personAndStaff: LiveData<PersonAndStaff> = _personAndStaff

    private val _staff: MutableLiveData<Staff> by lazy { MutableLiveData<Staff>() }
    val staff: LiveData<Staff> = _staff

    private val _allStaff: MutableLiveData<List<Staff>> by lazy { MutableLiveData<List<Staff>>() }
    val allStaff: LiveData<List<Staff>> = _allStaff


    init {
        allPerson()
    }

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

    fun getAllStaff(){
        viewModelScope.launch {
            repoStuff.getAllStaff().collect {
                _allStaff.value = it
            }
        }
    }

    // Display Person with  staff info
    fun getPersonAndStaff(id: Int){
        viewModelScope.launch {
            _personAndStaff.value = repoStuffAndPersons.getPersonAndStaff(id)
        }
    }

    private fun allPerson(){
        viewModelScope.launch {
            repoPerson.allPeron().collect { _allPerson.value = it }
        }
    }

    fun updatePerson(model: Person){
        viewModelScope.launch { repoPerson.updatePerson(model) }
    }

    fun getPerson(id: Int){
        viewModelScope.launch {
            repoPerson.getPerson(id).collect { _person.value = it }
        }
    }

    fun getStuff(id: Int){
        viewModelScope.launch {
            repoStuff.getStaff(id).collect { _staff.value = it  }
        }
    }
}