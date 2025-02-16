package com.robjonesdev.pf2elootsie.ui.navigation

import androidx.navigation.NavController
import java.util.UUID
import javax.inject.Inject

/**
 * A list of generic navigation actions that can be performed throughout
 * the application. Note that this class has no knowledge of
 * when or why these actions can be performed, and only exposes how
 * to perform them.
 */
class NavigationActions @Inject constructor(navController: NavController) {
    val navigateToHomeScreen: () -> Unit = {
        navController.navigate(NavDestinations.Home.route)
    }

    val navigateToLootListGeneration: () -> Unit = {
        navController.navigate(NavDestinations.LootListGeneration.route)
    }

    val navigateToLootDetailScreen: (lootListId: UUID) -> Unit = { lootListId ->
        navController.navigate(NavDestinations.LootDetail.route + "/$lootListId")
    }

    val navigateBack: () -> Unit = {
        navController.popBackStack()
    }
}