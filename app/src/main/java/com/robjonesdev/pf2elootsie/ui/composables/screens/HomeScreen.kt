package com.robjonesdev.pf2elootsie.ui.composables.screens


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.robjonesdev.pf2elootsie.R
import com.robjonesdev.pf2elootsie.data.models.LootList
import com.robjonesdev.pf2elootsie.ui.composables.CollapsedLootDetailCard
import com.robjonesdev.pf2elootsie.ui.composables.SavedLootListsColumn
import com.robjonesdev.pf2elootsie.ui.state.LootListUiState
import kotlinx.coroutines.flow.StateFlow

/**
 * Home screen composable for displaying previously generated loot lists
 * as well as exposing UI functionality for general navigation
 * around the application
 */
@Composable
fun HomeScreen(
    lootListsFlow: StateFlow<LootListUiState>,
    onNewLootButtonClick: () -> Unit,
    onLootListClick: (LootList) -> Unit,
) {
    val context = LocalContext.current
    val lootLists = lootListsFlow.collectAsState().value.lootLists

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = context.getString(R.string.Home_Activity_Generate_Loot_Label)) },
                onClick = { onNewLootButtonClick() },
                icon = { Icon(Icons.Filled.Add, "") }
            )
        }
    ) { paddingValues ->
        if (lootLists.isNotEmpty()) {
            SavedLootListsColumn(
                savedLootLists = lootLists,
                modifier = Modifier.padding(paddingValues)
            ) { itemContent ->
                CollapsedLootDetailCard(
                    listName = itemContent.name,
                    onListClick = { onLootListClick( itemContent ) }
                )
            }
        } else {
            Text(
                text = context.getString(R.string.Home_Activity_No_Loot_Generated_Label),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                style = MaterialTheme.typography.h6
            )
        }
    }
}