package com.robjonesdev.pf2elootsie.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.robjonesdev.pf2elootsie.data.models.LootGenerationPreferences
import com.robjonesdev.pf2elootsie.ui.composables.screens.HomeScreen
import com.robjonesdev.pf2elootsie.ui.composables.screens.LootDetailScreen
import com.robjonesdev.pf2elootsie.ui.composables.screens.LootListGenerationScreen
import com.robjonesdev.pf2elootsie.ui.viewmodel.LootListViewModel
import java.util.UUID

/**
 * A class responsible for maintaining a map of navigable screens
 * throughout the application, as well as wiring navigation actions
 * to composable screens through ui events that they expose
 */
@Composable
fun LootsieNavigation(
    navController: NavHostController = rememberNavController(),
    navigationActions: NavigationActions = NavigationActions(navController),
    onPreferencesSelected: (LootGenerationPreferences) -> Unit,
) {

    val lootListViewModel: LootListViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = NavDestinations.Home.route
    ) {
        composable(route = NavDestinations.Home.route) {
            HomeScreen(
                lootListsFlow = lootListViewModel.uiState,
                onNewLootButtonClick = {
                    navigationActions.navigateToLootListGeneration()
                },
                onLootListClick = { lootList ->
                    navigationActions.navigateToLootDetailScreen(lootList.uniqueID)
                }
            )
        }
        composable(route = NavDestinations.LootListGeneration.route) {
            LootListGenerationScreen(
                onPreferencesSelected = { generationPreferences ->
                    onPreferencesSelected(generationPreferences)
                    navigationActions.navigateBack()
                }
            )
        }
        composable(
            route = "${NavDestinations.LootDetail.route}/{${NavDestinations.LOOT_LIST_ID_ARG}}",
            arguments = listOf(
                navArgument(NavDestinations.LOOT_LIST_ID_ARG) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            LootDetailScreen(
                lootListId = UUID.fromString(backStackEntry.arguments?.getString(NavDestinations.LOOT_LIST_ID_ARG)) ?: UUID.fromString(""),
                activeLootLists = lootListViewModel.uiState.value.lootLists
            )
        }
    }
}
