package com.hcanyz.android_kit.widget.storage.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CommonPond(
    @PrimaryKey @ColumnInfo(name = "pond_key") val key: String,
    @ColumnInfo(name = "value") val value: String?
)