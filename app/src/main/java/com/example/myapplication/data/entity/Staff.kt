package com.example.myapplication.data.entity

import android.os.Parcelable
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.parcelize.Parcelize


@Entity(tableName = "staffs")
@Parcelize
data class Staff(
        @ColumnInfo(name = "job") val job: String,
        @ColumnInfo(name = "office") val office: String,
        @ColumnInfo(name = "_id", index = true)
        @PrimaryKey(autoGenerate = true) var id: Int = 0
) : Parcelable {
    @Dao
    interface Store{
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertStaff(model: Staff)

        @Delete
        suspend fun deleteStaff(model: Staff)

        @Update(onConflict = OnConflictStrategy.REPLACE)
        suspend fun updateStaff(model: Staff)

        @Query("SELECT * FROM `staffs` WHERE `_id` = :id")
        fun getStaff(id: Int): Flow<Staff>

        fun getStaffDistinct(id: Int) = getStaff(id).distinctUntilChanged()

        @Query("SELECT * FROM `staffs`")
        fun allStaff(): Flow<List<Staff>>

        fun allStaffDistinct() = allStaff().distinctUntilChanged()
    }
}
