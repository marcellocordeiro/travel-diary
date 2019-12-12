package com.myapp.traveldiary

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.myapp.traveldiary.dal.AppDatabase
import com.myapp.traveldiary.dal.dao.Diary
import com.myapp.traveldiary.dal.dao.DiaryDao
import com.myapp.traveldiary.dal.dao.Event
import com.myapp.traveldiary.dal.dao.EventDao
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class EventDaoTest {
    private lateinit var diaryDao: DiaryDao
    private lateinit var eventDao: EventDao
    private lateinit var db: AppDatabase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).build()
        diaryDao = db.diaryDao()
        eventDao = db.eventDao()

        diaryDao.insert(DIARY_1)
        diaryDao.insert(DIARY_2)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.clearAllTables()
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun givenEvent_shouldInsertItCorrectly() {
        val event = Event(DIARY_ID, "some-name", "some-location", 123, "some-path")

        eventDao.insert(event)

        assertThat(eventDao.selectAllEvents(DIARY_ID).blockingObserve()!![0], equalTo(event))
    }

    @Test
    @Throws(Exception::class)
    fun givenDiaryId_shouldReturnOnlyEventsOfThatDiaryId() {
        val event1 = Event(DIARY_ID, "some-name", "some-location", 123, "some-path")
        val event2 = Event(DIARY_ID_2, "some-name-2", "some-location-2", 1234, "some-path-2")

        eventDao.insert(event1)
        eventDao.insert(event2)

        assertThat(eventDao.selectAllEvents(DIARY_ID).blockingObserve()!!.size, equalTo(1))
        assertThat(eventDao.selectAllEvents(DIARY_ID).blockingObserve()!![0], equalTo(event1))
    }

    @Test
    @Throws(Exception::class)
    fun givenEventId_shouldDeleteIt() {
        val event = Event(DIARY_ID, "some-name", "some-location", 123, "some-path")
        val eventId = 1L

        eventDao.insert(event)

        eventDao.delete(eventId)

        assertThat(eventDao.selectAllEvents(DIARY_ID).blockingObserve()!!.size, equalTo(0))

    }

    @Test
    @Throws(Exception::class)
    fun givenEventId_shouldUpdateImagePath() {
        val event = Event(DIARY_ID, "some-name", "some-location", 123, "some-path")
        val newImagePath = "new-image-path"
        val eventId = 1L

        eventDao.insert(event)

        eventDao.updateImagePath(eventId, newImagePath)

        assertThat(eventDao.selectAllEvents(DIARY_ID).blockingObserve()!![0].imagePath, equalTo(newImagePath))

    }

    companion object {
        const val DIARY_ID = 1L
        const val DIARY_ID_2 = 2L
        val DIARY_1 = Diary("diary-name", "diary-location", 12345, 123456)
        val DIARY_2 = Diary("diary-name-2", "diary-location-2", 123456, 1234567)
    }
}

private fun <T> LiveData<T>.blockingObserve(): T? {
    var value: T? = null
    val latch = CountDownLatch(1)

    val observer = Observer<T> { t ->
        value = t
        latch.countDown()
    }

    observeForever(observer)

    latch.await(2, TimeUnit.SECONDS)
    return value
}
