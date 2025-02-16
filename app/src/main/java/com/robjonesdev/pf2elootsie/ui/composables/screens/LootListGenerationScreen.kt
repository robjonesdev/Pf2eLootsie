package com.robjonesdev.pf2elootsie.ui.composables.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.robjonesdev.pf2elootsie.R
import com.robjonesdev.pf2elootsie.data.models.EquipmentTraits
import com.robjonesdev.pf2elootsie.data.models.EquipmentRarities
import com.robjonesdev.pf2elootsie.data.models.LootGenerationPreferences
import com.robjonesdev.pf2elootsie.ui.composables.LootsieDropdown

/**
 * Composable class for generation of new loot lists, allowing
 * users to specify parameters for generation.
 * @param onPreferencesSelected: A method for returning generation preferences back to a calling class
 */
@Composable
fun LootListGenerationScreen(
    onPreferencesSelected: (LootGenerationPreferences) -> Unit,
) {
    var lootGenerationPreferences by remember { mutableStateOf(LootGenerationPreferences()) }
    val context = LocalContext.current

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(text = context.getString(R.string.Generation_Activity_Generate_New_List_Label), style = MaterialTheme.typography.h5, modifier = Modifier.padding(16.dp))

            OutlinedTextField(
                value = lootGenerationPreferences.lootListName,
                onValueChange = { input ->
                    lootGenerationPreferences = lootGenerationPreferences.copy(lootListName = input)
                },
                label = { Text(context.getString(R.string.Generation_Activity_Loot_List_Name_Label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = lootGenerationPreferences.targetLootLevel,
                onValueChange = { input ->
                    if (input.isDigitsOnly()) {
                        lootGenerationPreferences = lootGenerationPreferences.copy(targetLootLevel = input)
                    }
                },
                label = { Text(context.getString(R.string.Generation_Activity_Target_Loot_Level_Label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = lootGenerationPreferences.totalPriceLimit,
                onValueChange = { input ->
                    if (input.isEmpty() || input.toFloatOrNull() != null) {
                        lootGenerationPreferences = lootGenerationPreferences.copy(totalPriceLimit = input)
                    }
                },
                label = { Text(context.getString(R.string.Generation_Activity_Total_Price_Label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            LootsieDropdown(
                isExpanded = false,
                selected = lootGenerationPreferences.traits,
                onSelection = { equipmentCategory ->
                    lootGenerationPreferences = lootGenerationPreferences.copy(
                        traits = if (equipmentCategory in lootGenerationPreferences.traits) {
                            (lootGenerationPreferences.traits - equipmentCategory).toMutableList()
                        } else {
                            (lootGenerationPreferences.traits + equipmentCategory).toMutableList()
                        }
                    )
                },
                dropdownLabel = context.getString(R.string.Generation_Activity_Equipment_Traits_Label),
                items = EquipmentTraits.entries
            )

            LootsieDropdown(
                isExpanded = false,
                selected = lootGenerationPreferences.rarities,
                onSelection = { equipmentRarity ->
                    lootGenerationPreferences = lootGenerationPreferences.copy(
                        rarities = if (equipmentRarity in lootGenerationPreferences.rarities) {
                            (lootGenerationPreferences.rarities - equipmentRarity).toMutableList()
                        } else {
                            (lootGenerationPreferences.rarities + equipmentRarity).toMutableList()
                        }
                    )
                },
                dropdownLabel = context.getString(R.string.Generation_Activity_Eligible_Equipment_Rarities_Label),
                items = EquipmentRarities.entries
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onPreferencesSelected(lootGenerationPreferences) },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                enabled = lootGenerationPreferences.targetLootLevel.isNotEmpty() &&
                    lootGenerationPreferences.totalPriceLimit.isNotEmpty() &&
                    lootGenerationPreferences.rarities.isNotEmpty() &&
                    lootGenerationPreferences.traits.isNotEmpty()
            ) {
                Text(context.getString(R.string.Generation_Activity_Generate_Loot_Button_Label))
            }
        }
    }

}
