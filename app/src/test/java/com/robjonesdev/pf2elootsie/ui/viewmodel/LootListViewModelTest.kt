package com.robjonesdev.pf2elootsie.ui.viewmodel

import com.robjonesdev.pf2elootsie.UnitTestUtils
import com.robjonesdev.pf2elootsie.data.models.Equipment
import com.robjonesdev.pf2elootsie.data.models.LootList
import com.robjonesdev.pf2elootsie.ui.state.LootListUiState
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.UUID

@ExperimentalCoroutinesApi
class LootListViewModelTest {
    companion object {
        val NAME_FAKE = "First Loot List"
        val NAME_FAKE_TWO = "Second Loot List"
        val ID_FAKE = UUID.randomUUID()
        val ID_FAKE_TWO = UUID.randomUUID()
        val EQUIPMENT_FAKE = Equipment(
            name = "Example Equipment",
            id ="1",
        )
        val EQUIPMENT_FAKE_TWO = Equipment(
            name = "Example Equipment Two",
            id = "2",
        )
        val EXCEPTION_FAKE = Exception()
    }

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var mockLootListRepository: LootListRepository

    private lateinit var subject: LootListViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        subject = LootListViewModel(
            lootListRepository = mockLootListRepository
        )

        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `When addLootList is called with no saved lootLists, Then verify loot list is added to uiState`() = runTest {
        UnitTestUtils.verifyMockkExceptionDidNotOccur {
            val newLootList = LootList(
                name = NAME_FAKE,
                uniqueID = ID_FAKE,
                equipment = listOf(EQUIPMENT_FAKE)
            )

            val initialState = LootListUiState()
            subject.addLootList(newLootList)
            testDispatcher.scheduler.advanceUntilIdle()
            val uiState = subject.uiState.first()

            assertEquals("Loot list was not successfully added to the UI state when an add was called", initialState.copy(lootLists = listOf(newLootList)), uiState)

        }
    }

    @Test
    fun `When addLootList is called with existing saved lootLists, Then verify previous lists remain intact`() = runTest {
        UnitTestUtils.verifyMockkExceptionDidNotOccur {
            val previousSavedLootList = LootList(
                name = NAME_FAKE,
                uniqueID = ID_FAKE,
                equipment = listOf(EQUIPMENT_FAKE)
            )

            val newLootList = LootList(
                name = NAME_FAKE_TWO,
                uniqueID = ID_FAKE_TWO,
                equipment = listOf(EQUIPMENT_FAKE_TWO)
            )

            subject.addLootList(previousSavedLootList)
            testDispatcher.scheduler.advanceUntilIdle()

            subject.addLootList(newLootList)
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(
                "Previous loot lists were not maintained when an add was called",
                2,
                subject.uiState.value.lootLists.size
            )
        }
    }

    @Test
    fun `When clearLootLists is called with existing lootLists, Then verify all existing loot lists are removed`() = runTest{
        UnitTestUtils.verifyMockkExceptionDidNotOccur {
            val previousSavedLootList = LootList(
                name = NAME_FAKE,
                uniqueID = ID_FAKE,
                equipment = listOf(EQUIPMENT_FAKE)
            )

            subject.addLootList(previousSavedLootList)
            testDispatcher.scheduler.advanceUntilIdle()

            subject.clearLootLists()
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(
                "Previous loot lists were not cleared when a clear was called",
                0,
                subject.uiState.value.lootLists.size
            )
        }
    }

    @Test
    fun `When retrieveLootList is called, and the operation succeeds, then verify saved loot lists are appended to`() = runTest{
        UnitTestUtils.verifyMockkExceptionDidNotOccur {
            val previousSavedLootList = LootList(
                name = NAME_FAKE,
                uniqueID = ID_FAKE,
                equipment = listOf(EQUIPMENT_FAKE)
            )

            val newLootList = LootList(
                name = NAME_FAKE_TWO,
                uniqueID = ID_FAKE_TWO,
                equipment = listOf(EQUIPMENT_FAKE_TWO)
            )

            subject.addLootList(previousSavedLootList)
            testDispatcher.scheduler.advanceUntilIdle()

            coEvery { mockLootListRepository.retrieveLootLists() } returns listOf(newLootList)
            subject.retrieveLoot()
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(
                "Previous loot lists were not maintained when an retrieve was called",
                2,
                subject.uiState.value.lootLists.size
            )
            assertEquals(
                "UI state did not swap to indicate loading was finished when a retrieve had finished",
                false,
                subject.uiState.value.isLoading
            )
            assertEquals(
                "UI state indicated an error was present when a retrieve had finished successfully",
                null,
                subject.uiState.value.error
            )
        }
    }

    @Test
    fun `When retrieveLootList is called, and the operation fails, then verify saved loot lists are appended to`() = runTest{
        UnitTestUtils.verifyMockkExceptionDidNotOccur {
            val previousSavedLootList = LootList(
                name = NAME_FAKE,
                uniqueID = ID_FAKE,
                equipment = listOf(EQUIPMENT_FAKE)
            )

            subject.addLootList(previousSavedLootList)
            testDispatcher.scheduler.advanceUntilIdle()

            coEvery { mockLootListRepository.retrieveLootLists() } throws EXCEPTION_FAKE
            subject.retrieveLoot()
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(
                "A new loot list was added when a retrieve call failed",
                1,
                subject.uiState.value.lootLists.size
            )
            assertEquals(
                "UI state did not swap to indicate loading was finished when a retrieve had finished",
                false,
                subject.uiState.value.isLoading
            )
            assertEquals(
                "UI state indicated no error was present when a retrieve had finished with an error",
                EXCEPTION_FAKE,
                subject.uiState.value.error
            )
        }
    }
}
