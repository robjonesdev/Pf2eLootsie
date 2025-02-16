package com.robjonesdev.pf2elootsie.ui.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.robjonesdev.pf2elootsie.data.models.LootList

@Composable
fun SavedLootListsColumn(
    savedLootLists: List<LootList>,
    modifier: Modifier = Modifier,
    itemContent: @Composable (LootList) -> Unit
) {
    LazyColumn(modifier = modifier.then(Modifier.fillMaxSize())) {
        items(
            items = savedLootLists,
            key = { it.uniqueID }
        ) { lootList ->
            itemContent(lootList)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SavedLootListsColumnPreview() {

}
