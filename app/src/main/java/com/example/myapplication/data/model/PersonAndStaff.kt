package com.example.myapplication.data.model

import android.os.Parcelable
import androidx.room.*
import com.example.myapplication.data.entity.Person
import com.example.myapplication.data.entity.Staff
import kotlinx.parcelize.Parcelize

@Parcelize
data class PersonAndStaff(
    @Embedded val person: Person,
    @Relation(
        parentColumn = "staff_id",
        entityColumn = "_id")
    val staff: Staff
): Parcelable{
    @Dao
    interface Store{
        @Transaction
        @Query("SELECT * FROM `persons` WHERE `_id` = :id")
        suspend fun getPersonAndStaff(id: Int): PersonAndStaff
    }
}
