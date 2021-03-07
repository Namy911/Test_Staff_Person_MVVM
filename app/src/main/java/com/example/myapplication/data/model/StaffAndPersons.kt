package com.example.myapplication.data.model

import android.os.Parcelable
import androidx.room.*
import com.example.myapplication.data.entity.Person
import com.example.myapplication.data.entity.Staff
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.parcelize.Parcelize

@Parcelize
data class StaffAndPersons(
    @Embedded val staff: Staff,
    @Relation(
                parentColumn = "_id",
                entityColumn = "staff_id"
        )
        val persons: List<Person>
) : Parcelable {
        @Dao
        interface Store {
                @Transaction
                @Query("SELECT * FROM `staffs` WHERE `_id` = :id")
                fun getPersonsByStaff(id: Int): Flow<StaffAndPersons>

                fun getPersonsByStaffDistinct(id: Int) = getPersonsByStaff(id).distinctUntilChanged()

                @Transaction
                @Query("SELECT * FROM `staffs`")
                fun loadAllStaff(): Flow<List<StaffAndPersons>>

                fun loadAllStaffDistinct() = loadAllStaff().distinctUntilChanged()

                @Transaction
                @Query("SELECT * FROM `staffs` WHERE _id = :id")
                suspend fun getPersonAndStaff(id: Int): StaffAndPersons
        }
}