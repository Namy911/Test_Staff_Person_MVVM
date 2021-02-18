package com.example.myapplication.repository

import com.example.myapplication.data.model.PersonAndStaff
import com.example.myapplication.data.model.StaffAndPersons
import javax.inject.Inject

class StaffAndPersonsRepository @Inject constructor(
        private val staffAndPers: StaffAndPersons.Store,
        private val persAndStaff: PersonAndStaff.Store
) {

        fun loadAllStaff() = staffAndPers.loadAllStaff()
        fun getPersonsByStaff(id: Int) = staffAndPers.getPersonsByStaff(id)


        suspend fun getPersonAndStaff(id: Int) = persAndStaff.getPersonAndStaff(id)

}