package com.hcanyz.android_kit.widget.storage.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CommonPond::class], version = 1)
abstract class CommonDb : RoomDatabase() {
    abstract fun commonPondDao(): CommonPondDao
}