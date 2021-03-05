package com.example.myapplication.data.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration2To3: Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) { }
}