package com.robjonesdev.pf2elootsie.ui.composables.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.robjonesdev.pf2elootsie.data.models.Equipment
import com.robjonesdev.pf2elootsie.data.models.EquipmentProperty
import com.robjonesdev.pf2elootsie.data.models.LootGenerationPreferences
import com.robjonesdev.pf2elootsie.data.models.LootList
import com.robjonesdev.pf2elootsie.ui.composables.TaggedText
import java.util.UUID
import java.util.regex.Pattern

/**
 * Composable class for inspecting generated
 * [com.robjonesdev.pf2elootsie.data.models.LootList] in greater detail.
 */
@Composable
fun LootDetailScreen(
    lootListId: UUID,
    activeLootLists: List<LootList>,
) {
    val context = LocalContext.current
    val lootList:LootList? = run {
        for (lootList in activeLootLists) {
            if (lootList.uniqueID == lootListId) {
                return@run lootList
            }
        }
        null
    }

    var expandedCardId by remember { mutableStateOf<String?>(null) }


    Scaffold { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {

            lootList?.let {
                Text(text = lootList.name, style = MaterialTheme.typography.h5, modifier = Modifier.padding(16.dp))

                if(lootList.equipment.isNotEmpty()) {
                    for(equipment in lootList.equipment){
                        val isExpanded = expandedCardId == equipment.equipmentId

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expandedCardId = if(isExpanded) { null } else { equipment.equipmentId }}
                                .animateContentSize(),
                            elevation = 4.dp,
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(text = equipment.name, style = MaterialTheme.typography.body1)
                                AnimatedVisibility(visible = isExpanded) {
                                    Column {
                                        val characteristics = listOfNotNull(
                                            EquipmentProperty.LEVEL.label to equipment.level.takeIf { it > -1 }?.toString(),
                                            EquipmentProperty.TRAITS.label to equipment.traits.takeIf { it.isNotEmpty() }?.joinToString(", "),
                                            EquipmentProperty.RARITY.label to equipment.rarity.takeIf { it.isNotBlank() },
                                            EquipmentProperty.PRICE.label to equipment.price.takeIf { it > -0.01f }?.let { "$it gp" },
                                            EquipmentProperty.SIZE.label to equipment.size.takeIf { it.isNotBlank() },
                                            EquipmentProperty.USAGE.label to equipment.usage.takeIf { it.isNotBlank() },
                                            EquipmentProperty.TYPE.label to equipment.type.takeIf { it.isNotBlank() },
                                            EquipmentProperty.BASE_ITEM.label to equipment.baseItem.takeIf { !it.isNullOrBlank() },
                                            EquipmentProperty.MATERIAL_GRADE.label to equipment.materialGrade.takeIf { !it.isNullOrBlank() },
                                            EquipmentProperty.MATERIAL_TYPE.label to equipment.materialType.takeIf { !it.isNullOrBlank() },
                                            EquipmentProperty.HARDNESS.label to equipment.hardness.takeIf { it > 0 }?.toString(),
                                            EquipmentProperty.MAX_HP.label to equipment.maxHP.takeIf { it > 0 }?.toString(),
                                            EquipmentProperty.DESCRIPTION.label to equipment.description.takeIf { it.isNotBlank() }
                                        )

                                        for ((label, value) in characteristics) {
                                            if(value!=null) {
                                                val uuidPattern = Pattern.compile("@UUID\\[[^]]+\\]")
                                                val cleanedText = uuidPattern.matcher(value).replaceAll("")
                                                TaggedText(text = "$label: $cleanedText")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Text(text = "No equipment matching the specified criteria could be generated. Please try again with different preferences.", style = MaterialTheme.typography.h5, modifier = Modifier.padding(16.dp))
                }

            } ?: run {
                Text(text = "Something went wrong, Please try again.", style = MaterialTheme.typography.h5, modifier = Modifier.padding(16.dp))
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun LootDetailScreenPreview() {
    val mockLootListId = UUID.randomUUID()
    val previewLootLists = listOf(
        LootList(
            uniqueID = mockLootListId,
            name = "Dungeon Hoard",
            equipment = listOf(
                Equipment(
                    equipmentId = UUID.randomUUID().toString(),
                    name = "Sword of Fire"
                ),
                Equipment(
                    equipmentId = UUID.randomUUID().toString(),
                    name = "Shield of Light"
                ),
            ),
            generationPreferences = LootGenerationPreferences()
        )
    )

    LootDetailScreen(lootListId = mockLootListId, activeLootLists = previewLootLists)
}