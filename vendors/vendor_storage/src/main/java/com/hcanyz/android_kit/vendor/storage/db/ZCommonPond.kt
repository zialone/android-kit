package com.hcanyz.android_kit.vendor.storage.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ZCommonPond(
    @PrimaryKey @ColumnInfo(name = "pond_key") val key: String,
    @ColumnInfo(name = "value") val value: String?
)