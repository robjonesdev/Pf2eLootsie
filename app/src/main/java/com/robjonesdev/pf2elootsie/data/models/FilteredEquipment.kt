package com.robjonesdev.pf2elootsie.data.models

data class FilteredEquipment(
    val filteredEquipment: List<Equipment> = listOf(),
    val generationPreferences: LootGenerationPreferences = LootGenerationPreferences()
)