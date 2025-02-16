package com.robjonesdev.pf2elootsie.data.models

import java.util.UUID

/**
 * A class representing a named list of previously generated equipment
 * @param uniqueID: A randomly generated UUID used for identifying this list instance
 * @param name: The name of the list
 * @param equipment: The equipment associated with this named list
 * @param generationPreferences: The preferences used to generate this loot list
 */
data class LootList(
    val uniqueID: UUID,
    val name: String,
    val equipment: List<Equipment>,
    val generationPreferences: LootGenerationPreferences,
)