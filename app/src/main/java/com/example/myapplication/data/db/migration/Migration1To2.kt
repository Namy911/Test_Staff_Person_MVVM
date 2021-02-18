package com.example.myapplication.data.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration1To2: Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(" ALTER TABLE `persons` ADD COLUMN `date` INTEGER  NOT NULL DEFAULT 1601640082")
    }
}