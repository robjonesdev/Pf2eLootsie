package com.robjonesdev.pf2elootsie.data.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.robjonesdev.pf2elootsie.data.models.Equipment
import com.robjonesdev.pf2elootsie.data.models.EquipmentWithTraits
import com.robjonesdev.pf2elootsie.data.models.Trait

/**
 * A data access object for long term CRUD operations with a database
 * for [com.robjonesdev.pf2elootsie.data.models.Equipment] objects
 */
@Dao
interface EquipmentDao {

    @Query("SELECT COUNT(*) FROM equipment")
    suspend fun getEquipmentCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEquipment(equipment: Equipment)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrait(trait: Trait)

    @Query("SELECT * FROM equipment")
    fun getAllEquipment(): List<Equipment>

    @Transaction
    @Query("SELECT * FROM equipment")
    fun getAllEquipmentWithTraits() : List<EquipmentWithTraits>

    @Transaction
    @Query(
        """
    SELECT * FROM equipment
    WHERE level <= :targetLootLevel
    AND price <= :totalPriceLimit
    AND LOWER(rarity) IN (:rarities)
    AND equipmentId IN (
        SELECT equipmentOwnerId FROM trait WHERE LOWER(name) IN (:traits)
    )
    """
    )
    fun getFilteredEquipmentWithTraits(
        targetLootLevel: Int,
        totalPriceLimit: Float,
        rarities: List<String>,
        traits: List<String>
    ): List<EquipmentWithTraits>

}
