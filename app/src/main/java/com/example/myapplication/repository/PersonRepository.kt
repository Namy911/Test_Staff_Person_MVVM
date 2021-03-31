package com.example.myapplication.repository

import com.example.myapplication.data.entity.Person
import javax.inject.Inject

class PersonRepository @Inject constructor(private val store: Person.Store) {

    fun allPerson() = store.allPerson()

    fun getPerson(id: Int) = store.getPersonDistinct(id)

    suspend fun insertPerson(model: Person){ store.insertPerson(model) }

    suspend fun deletePerson(model: Person) { store.deletePerson(model) }

    suspend fun updatePerson(model: Person) { store.updatePerson(model) }
}