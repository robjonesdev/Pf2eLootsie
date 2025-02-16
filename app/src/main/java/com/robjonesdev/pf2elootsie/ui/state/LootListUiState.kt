package com.robjonesdev.pf2elootsie.ui.state

import com.robjonesdev.pf2elootsie.data.models.LootList

/**
 * state data for saved loot lists for the UI to consume from
 */
data class LootListUiState(
    val lootLists: List<LootList> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null
)