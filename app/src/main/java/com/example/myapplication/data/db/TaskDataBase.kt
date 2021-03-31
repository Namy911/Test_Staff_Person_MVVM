package com.example.myapplication.data.db


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.data.db.util.Converters
import com.example.myapplication.data.entity.Data
import com.example.myapplication.data.entity.Person
import com.example.myapplication.data.model.PersonAndStaff
import com.example.myapplication.data.entity.Staff
import com.example.myapplication.data.model.StaffAndPersons


@Database(entities = [Person::class, Staff::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TaskDataBase: RoomDatabase() {

    abstract fun personStore(): Person.Store
    abstract fun staffStore(): Staff.Store
    abstract fun staffAndPersonsStore(): StaffAndPersons.Store
    abstract fun personAndStaffStore(): PersonAndStaff.Store
    abstract fun userStore(): Data.Store
}
class PrepopulateDataBase: RoomDatabase.Callback(){
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        db.execSQL("""
            INSERT INTO `persons`(name, sur_name, staff_id) VALUES ("name1", "last Name 1", 1),("name2", "last Name 2", 3), ("name3", "last Name 3", 3),
                                                                    ("name4", "last Name 4", 1), ("name5", "last Name 5", 2), ("name6", "last Name 6", 2);
        """.trimIndent())
        db.execSQL("""
            INSERT INTO `staffs`(job, office) VALUES("job 1", "nrt 911"), ("job 2", "nrt 9051"), ("job 3", "nrt 912"), ("job 8", "nrt 9331");
        """.trimIndent())
    }
}