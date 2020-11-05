package com.hcanyz.android_kit.vendor.storage

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hcanyz.android_kit.vendor.storage.db.ZDbCommon
import com.hcanyz.android_kit.vendor.storage.db.ZCommonPond
import com.hcanyz.android_kit.vendor.storage.db.ZDaoCommonPond
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ZDbCommonTest {
    private lateinit var zDaoCommonPond: ZDaoCommonPond
    private lateinit var dbCommon: ZDbCommon

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbCommon = Room.inMemoryDatabaseBuilder(
            context, ZDbCommon::class.java
        ).build()
        zDaoCommonPond = dbCommon.commonPondDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        dbCommon.close()
    }

    @Test
    @Throws(Exception::class)
    fun pondDaoTest() {
        val pondDao = dbCommon.commonPondDao()
        pondDao.insertOrUpdate(ZCommonPond("feature_login_accountInfo", "{}"))
        pondDao.insertOrUpdate(ZCommonPond("feature_login_accountInfo_135xxxxxxxx", "{\"name\":\"135\"}"))
        pondDao.insertOrUpdate(ZCommonPond("feature_login_accountInfo_152xxxxxxxx", "{}"))
        pondDao.insertOrUpdate(ZCommonPond("vendor_config_global_config", "{}"))

        val all = pondDao.getAll()
        assertThat(all.size, equalTo(4))

        val accountInfo135 = pondDao.loadByKey("feature_login_accountInfo_135xxxxxxxx")
        assertThat(accountInfo135.value, equalTo("{\"name\":\"135\"}"))

        pondDao.delete(accountInfo135)
        assertThat(
            pondDao.loadByKey("feature_login_accountInfo_135xxxxxxxx"),
            equalTo<ZCommonPond>(null)
        )

        val finByKeyPrefix = pondDao.finByKeyPrefix("feature_login_accountInfo")
        assertThat(finByKeyPrefix.size, equalTo(2))
    }
}
