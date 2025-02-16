package com.robjonesdev.pf2elootsie.data.models

import androidx.compose.runtime.mutableStateListOf

/**
 * A class representing a set of preferences for generating a new loot list
 * @param lootListName: The desired name for the generated loot list
 * @param targetLootLevel: The desired level of equipment for this generation
 * @param totalPriceLimit: An upper bound on total price of all equipment for this generation
 * @param traits: A set of [com.robjonesdev.pf2elootsie.data.models.EquipmentTraits] to include in this generation
 * @param rarities: A set of [com.robjonesdev.pf2elootsie.data.models.EquipmentRarities] to include in this generation
 */
data class LootGenerationPreferences(
    var lootListName: String = "New Loot List",
    var targetLootLevel: String = "5",
    var totalPriceLimit: String = "25",
    var traits: MutableList<EquipmentTraits> = mutableStateListOf(EquipmentTraits.Invested),
    var rarities: MutableList<EquipmentRarities> = mutableStateListOf(EquipmentRarities.Common),
)