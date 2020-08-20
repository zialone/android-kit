package com.hcanyz.android_kit.widget.storage.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CommonPondDao {
    @Insert
    fun insert(users: CommonPond)

    @Delete
    fun delete(user: CommonPond)

    @Query("SELECT * FROM CommonPond")
    fun getAll(): List<CommonPond>

    @Query("SELECT * FROM CommonPond")
    fun getAllLiveData(): LiveData<List<CommonPond>>

    @Query("SELECT * FROM CommonPond WHERE pond_key LIKE :key || '%'")
    fun finByKeyPrefix(key: String): List<CommonPond>

    @Query("SELECT * FROM CommonPond WHERE pond_key LIKE :key || '%'")
    fun finByKeyPrefixLiveData(key: String): LiveData<List<CommonPond>>

    @Query("SELECT * FROM CommonPond WHERE pond_key = :key")
    fun loadByKey(key: String): CommonPond

    @Query("SELECT * FROM CommonPond WHERE pond_key = :key")
    fun loadByKeyLiveData(key: String): LiveData<CommonPond>
}