package com.example.myapplication.data.entity

import android.net.Uri
import android.os.Parcelable
import androidx.paging.PagingSource
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import androidx.room.ForeignKey.SET_NULL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.parcelize.Parcelize
import java.util.*


@Entity(tableName = "persons",
    foreignKeys = [ForeignKey(
        entity = Staff::class,
        parentColumns = arrayOf("_id"),
        childColumns = arrayOf("staff_id"),
        onUpdate = CASCADE,
        onDelete = CASCADE
    )])
@Parcelize
data class Person(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "sur_name") val surName: String,
    @ColumnInfo(name = "date", defaultValue = "1601640082") var date: Date,
    @ColumnInfo(name = "staff_id", index = true) val staffId: Int,
    @ColumnInfo(name = "_id", index = true)
    @PrimaryKey(autoGenerate = true) var id: Int = 0
): Parcelable {
    @Dao
    interface Store{
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertPerson(model: Person)

        @Delete
        suspend fun deletePerson(model: Person)

        @Update(onConflict = OnConflictStrategy.REPLACE)
        suspend fun updatePerson(model: Person)

        @Query("SELECT * FROM `persons` WHERE `_id` = :id")
        fun getPerson(id: Int): Flow<Person>

        fun getPersonDistinct(id: Int) =  getPerson(id).distinctUntilChanged()

        @Query("SELECT * FROM `persons`")
        fun allPerson(): PagingSource<Int, Person>
    }
}
