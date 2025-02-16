package com.robjonesdev.pf2elootsie.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A composable class for displaying a shorthand or collapsed
 * card which displays only basic information of a loot list
 * optimized for display in a list
 */
@Composable
fun CollapsedLootDetailCard(
    listName: String,
    onListClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onListClick() },
        elevation = 4.dp
    ) {
        Text(
            text = listName,
            modifier = Modifier
                .padding(16.dp),
            style = MaterialTheme.typography.h6
        )
    }
}

@Preview
@Composable
fun LootListEntryPreview() {
    CollapsedLootDetailCard(
        listName = "Example Loot List",
        onListClick = {}
    )
}