package com.example.myapplication.repository

import com.example.myapplication.data.entity.Staff
import javax.inject.Inject

class StaffRepository @Inject constructor( private val repo: Staff.Store) {

    fun getAllStaff() = repo.allStaffDistinct()

    fun getStaff(id: Int) = repo.getStaffDistinct(id)
}
