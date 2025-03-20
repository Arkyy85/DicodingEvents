package com.ammar.core.data

import com.ammar.core.data.source.local.LocalDataSource
import com.ammar.core.data.source.local.entity.EventsEntity
import com.ammar.core.data.source.remote.RemoteDataSource
import com.ammar.core.data.source.remote.response.ListEventsItem
import com.ammar.core.data.source.remote.retrofit.ApiResponse
import com.ammar.core.domain.model.Events
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class EventsRepositoryTest {

    // Test dispatcher untuk coroutine
    private val testDispatcher = TestCoroutineDispatcher()

    // Mock dependencies
    private val mockRemoteDataSource: RemoteDataSource = mockk()
    private val mockLocalDataSource: LocalDataSource = mockk()

    // Subject under test
    private lateinit var eventsRepository: EventsRepository

    @BeforeEach
    fun setUp() {
        // Set dispatcher untuk coroutine testing
        Dispatchers.setMain(testDispatcher)

        // Inisialisasi repository dengan mock dependencies
        eventsRepository = EventsRepository(mockRemoteDataSource, mockLocalDataSource)
    }

    @AfterEach
    fun tearDown() {
        // Reset dispatcher setelah testing
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `getUpcomingEvents should return Flow of Resource with data from local when not stale`() = runTest {
        // Arrange
        val eventsEntities = listOf(
            EventsEntity(id = 1, name = "Event 1", eventType = "upcoming")
        )
        val events = listOf(
            Events(id = 1, name = "Event 1", eventType = "upcoming")
        )
        coEvery { mockLocalDataSource.getEvents("upcoming") } returns flowOf(eventsEntities)
        coEvery { mockLocalDataSource.getLastFetchTime() } returns flowOf(System.currentTimeMillis())

        // Act
        val result = eventsRepository.getUpcomingEvents().first()

        // Assert
        assertTrue(result is Resource.Success)
        assertEquals(events, (result as Resource.Success).data)
        coVerify(exactly = 0) { mockRemoteDataSource.getActiveEvents() }
    }

    @Test
    fun `getUpcomingEvents should fetch from remote when data is stale`() = runTest {
        // Arrange
        val eventsEntities = listOf(
            EventsEntity(id = 1, name = "Event 1", eventType = "upcoming")
        )
        val events = listOf(
            Events(id = 1, name = "Event 1", eventType = "upcoming")
        )
        val remoteData = listOf(
            ListEventsItem(
                id = 1,
                name = "Event 1",
                beginTime = "2023-10-01",
                category = "Tech",
                cityName = "Jakarta",
                description = "Description",
                endTime = "2023-10-02",
                imageLogo = "image.jpg",
                link = "https://example.com",
                mediaCover = "cover.jpg",
                ownerName = "Owner",
                quota = 100,
                registrants = 50,
                summary = "Summary"
            )
        )
        coEvery { mockLocalDataSource.getEvents("upcoming") } returns flowOf(eventsEntities)
        coEvery { mockLocalDataSource.getLastFetchTime() } returns flowOf(0) // Data stale
        coEvery { mockRemoteDataSource.getActiveEvents() } returns flowOf(ApiResponse.Success(remoteData))
        coEvery { mockLocalDataSource.insertEvents(any()) } returns Unit
        coEvery { mockLocalDataSource.saveLastFetchTime(any()) } returns Unit

        // Act
        val result = eventsRepository.getUpcomingEvents().first()

        // Assert
        assertTrue(result is Resource.Success)
        assertEquals(events, (result as Resource.Success).data)
        coVerify(exactly = 1) { mockRemoteDataSource.getActiveEvents() }
    }

    @Test
    fun `getFinishedEvents should return Flow of Resource with data from local when not stale`() = runTest {
        // Arrange
        val eventsEntities = listOf(
            EventsEntity(id = 1, name = "Event 1", eventType = "past")
        )
        val events = listOf(
            Events(id = 1, name = "Event 1", eventType = "past")
        )
        coEvery { mockLocalDataSource.getEvents("past") } returns flowOf(eventsEntities)
        coEvery { mockLocalDataSource.getLastFetchTime() } returns flowOf(System.currentTimeMillis())

        // Act
        val result = eventsRepository.getFinishedEvents().first()

        // Assert
        assertTrue(result is Resource.Success)
        assertEquals(events, (result as Resource.Success).data)
        coVerify(exactly = 0) { mockRemoteDataSource.getFinishedEvents() }
    }

    @Test
    fun `getSearchEvents should return Flow of Resource with data from local`() = runTest {
        // Arrange
        val eventsEntities = listOf(
            EventsEntity(id = 1, name = "Event 1", eventType = "upcoming")
        )
        val events = listOf(
            Events(id = 1, name = "Event 1", eventType = "upcoming")
        )
        coEvery { mockLocalDataSource.getSearchEvents(any()) } returns flowOf(eventsEntities)

        // Act
        val result = eventsRepository.getSearchEvents("query").first()

        // Assert
        assertTrue(result is Resource.Success)
        assertEquals(events, (result as Resource.Success).data)
    }

    @Test
    fun `getFavoriteEvents should return Flow of favorite events`() = runTest {
        // Arrange
        val eventsEntities = listOf(
            EventsEntity(id = 1, name = "Event 1", eventType = "upcoming")
        )
        val events = listOf(
            Events(id = 1, name = "Event 1", eventType = "upcoming")
        )
        coEvery { mockLocalDataSource.getFavoriteEvents() } returns flowOf(eventsEntities)

        // Act
        val result = eventsRepository.getFavoriteEvents().first()

        // Assert
        assertEquals(events, result)
    }

    @Test
    fun `setFavoriteEvents should call local data source`() = runTest {
        // Arrange
        val event = Events(id = 1, name = "Event 1", eventType = "upcoming")
        val eventEntity = EventsEntity(id = 1, name = "Event 1", eventType = "upcoming")
        coEvery { mockLocalDataSource.setFavoriteEvents(eventEntity, any()) } returns Unit

        // Act
        eventsRepository.setFavoriteEvents(event, true, "upcoming")

        // Assert
        coVerify(exactly = 1) { mockLocalDataSource.setFavoriteEvents(eventEntity, true) }
    }

    @Test
    fun `getThemeSetting should return Flow of theme setting`() = runTest {
        // Arrange
        coEvery { mockLocalDataSource.getThemeSetting() } returns flowOf(true)

        // Act
        val result = eventsRepository.getThemeSetting().first()

        // Assert
        assertTrue(result)
    }

    @Test
    fun `saveThemeSetting should call local data source`() = runTest {
        // Arrange
        coEvery { mockLocalDataSource.saveThemeSetting(any()) } returns Unit

        // Act
        eventsRepository.saveThemeSetting(true)

        // Assert
        coVerify(exactly = 1) { mockLocalDataSource.saveThemeSetting(true) }
    }
}