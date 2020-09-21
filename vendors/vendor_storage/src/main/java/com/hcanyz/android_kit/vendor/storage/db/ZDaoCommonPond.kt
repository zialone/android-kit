package com.hcanyz.android_kit.vendor.storage.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ZDaoCommonPond {
    @Insert
    fun insert(users: ZCommonPond)

    @Delete
    fun delete(user: ZCommonPond)

    @Query("SELECT * FROM ZCommonPond")
    fun getAll(): List<ZCommonPond>

    @Query("SELECT * FROM ZCommonPond")
    fun getAllLiveData(): LiveData<List<ZCommonPond>>

    @Query("SELECT * FROM ZCommonPond WHERE pond_key LIKE :key || '%'")
    fun finByKeyPrefix(key: String): List<ZCommonPond>

    @Query("SELECT * FROM ZCommonPond WHERE pond_key LIKE :key || '%'")
    fun finByKeyPrefixLiveData(key: String): LiveData<List<ZCommonPond>>

    @Query("SELECT * FROM ZCommonPond WHERE pond_key = :key")
    fun loadByKey(key: String): ZCommonPond

    @Query("SELECT * FROM ZCommonPond WHERE pond_key = :key")
    fun loadByKeyLiveData(key: String): LiveData<ZCommonPond>
}