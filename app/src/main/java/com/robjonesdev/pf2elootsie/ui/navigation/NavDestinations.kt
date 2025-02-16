package com.robjonesdev.pf2elootsie.ui.navigation

/**
 * A list of UI based navigation destinations across the application
 */
sealed class NavDestinations(val route: String) {
    companion object {
        val LOOT_LIST_ID_ARG = "lootListId"
    }

    data object Home : NavDestinations("Home")
    data object LootListGeneration : NavDestinations("LootListGeneration")
    data object LootDetail: NavDestinations("LootDetail")
}