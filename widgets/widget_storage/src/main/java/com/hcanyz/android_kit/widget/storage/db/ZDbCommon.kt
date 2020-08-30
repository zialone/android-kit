package com.hcanyz.android_kit.widget.storage.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ZCommonPond::class], version = 1)
abstract class ZDbCommon : RoomDatabase() {
    abstract fun commonPondDao(): ZDaoCommonPond
}