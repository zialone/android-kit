package com.hcanyz.android_kit.widget.storage

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hcanyz.android_kit.widget.storage.db.CommonDb
import com.hcanyz.android_kit.widget.storage.db.CommonPond
import com.hcanyz.android_kit.widget.storage.db.CommonPondDao
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
class CommonDbTest {
    private lateinit var commonPondDao: CommonPondDao
    private lateinit var db: CommonDb

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, CommonDb::class.java
        ).build()
        commonPondDao = db.commonPondDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun pondDaoTest() {
        val pondDao = db.commonPondDao()
        pondDao.insert(CommonPond("feature_login_accountInfo", "{}"))
        pondDao.insert(CommonPond("feature_login_accountInfo_135xxxxxxxx", "{\"name\":\"135\"}"))
        pondDao.insert(CommonPond("feature_login_accountInfo_152xxxxxxxx", "{}"))
        pondDao.insert(CommonPond("vendor_config_global_config", "{}"))

        val all = pondDao.getAll()
        assertThat(all.size, equalTo(4))

        val accountInfo135 = pondDao.loadByKey("feature_login_accountInfo_135xxxxxxxx")
        assertThat(accountInfo135.value, equalTo("{\"name\":\"135\"}"))

        pondDao.delete(accountInfo135)
        assertThat(
            pondDao.loadByKey("feature_login_accountInfo_135xxxxxxxx"),
            equalTo<CommonPond>(null)
        )

        val finByKeyPrefix = pondDao.finByKeyPrefix("feature_login_accountInfo")
        assertThat(finByKeyPrefix.size, equalTo(2))
    }
}
