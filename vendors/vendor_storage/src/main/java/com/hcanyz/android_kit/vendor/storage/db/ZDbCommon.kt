package com.hcanyz.android_kit.vendor.storage.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ZCommonPond::class], version = 1, exportSchema = false)
abstract class ZDbCommon : RoomDatabase() {
    abstract fun commonPondDao(): ZDaoCommonPond
}