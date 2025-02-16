package com.robjonesdev.pf2elootsie.ui.navigation

import androidx.navigation.NavHostController
import com.robjonesdev.pf2elootsie.UnitTestUtils
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.just
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationActionsTest {
    companion object {

    }

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var mockNavHostController: NavHostController

    lateinit var subject: NavigationActions

    @Before
    fun setup() {
        subject = NavigationActions(
            navController = mockNavHostController
        )
    }

    @Test
    fun `When performing navigation to the home screen, Then verify a nav controller call to home screen is made`() {
        UnitTestUtils.verifyMockkExceptionDidNotOccur {
            every { mockNavHostController.navigate(NavDestinations.Home.route) } just Runs
            subject.navigateToHomeScreen()
            verify(exactly = 1) { mockNavHostController.navigate(NavDestinations.Home.route) }
            verify(exactly = 0) { mockNavHostController.navigate(NavDestinations.LootListGeneration.route) }
            verify(exactly = 0) { mockNavHostController.popBackStack() }
        }
    }

    @Test
    fun `When performing navigation to the loot list generation screen, Then verify a nav controller call to the loot list generation screen is made`() {
        UnitTestUtils.verifyMockkExceptionDidNotOccur {
            every { mockNavHostController.navigate(NavDestinations.LootListGeneration.route) } just Runs
            subject.navigateToLootListGeneration
            verify(exactly = 0) { mockNavHostController.navigate(NavDestinations.Home.route) }
            verify(exactly = 1) { mockNavHostController.navigate(NavDestinations.LootListGeneration.route) }
            verify(exactly = 0) { mockNavHostController.popBackStack() }
        }
    }

    @Test
    fun `When performing back navigation, Then verify a nav controller call for popping the back stack occurred`() {
        UnitTestUtils.verifyMockkExceptionDidNotOccur {
            every { mockNavHostController.navigate(NavDestinations.LootListGeneration.route) } just Runs
            subject.navigateBack
            verify(exactly = 0) { mockNavHostController.navigate(NavDestinations.Home.route) }
            verify(exactly = 0) { mockNavHostController.navigate(NavDestinations.LootListGeneration.route) }
            verify(exactly = 1) { mockNavHostController.popBackStack() }
        }
    }
}