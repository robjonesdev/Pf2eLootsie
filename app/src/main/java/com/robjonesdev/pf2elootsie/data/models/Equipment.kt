package com.robjonesdev.pf2elootsie.data.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Equipment(
    @PrimaryKey val equipmentId: String,
    val name: String,
    val img: String? = "",
    val type: String = "",
    val baseItem: String? = "",
    val containerId: String? = "",
    val description: String = "",
    val hardness: Int = 0,
    val maxHP: Int = 0,
    val level: Int = 0,
    val materialGrade: String? = "",
    val materialType: String? = "",
    val price: Float = 0.00f,
    val license: String = "",
    val remaster: Boolean = false,
    val title: String = "",
    val rarity: String = "",
    val usage: String = "",
    val size: String = "",
    val category: String = "",
) {
    @Ignore
    var traits: List<String> = listOf()

    constructor(
        equipmentId: String,
        name: String,
        img: String?,
        type: String,
        baseItem: String?,
        containerId: String?,
        description: String,
        hardness: Int,
        maxHP: Int,
        level: Int,
        materialGrade: String?,
        materialType: String?,
        price: Float,
        license: String,
        remaster: Boolean,
        title: String,
        rarity: String,
        usage: String,
        size: String,
        traits: List<String>,
        category: String,
    ) : this(
        equipmentId, name, img, type, baseItem, containerId, description, hardness, maxHP, level,
        materialGrade, materialType, price, license, remaster, title, rarity, usage, size
    ) {
        this.traits = traits
    }
}

@Entity
data class Trait(
    val name: String,
    val equipmentOwnerId: String,
    @PrimaryKey(autoGenerate = true)
    val traitId: Long = 0L
)

data class EquipmentWithTraits(
    @Embedded val equipment: Equipment,
    @Relation(
        parentColumn = "equipmentId",
        entityColumn = "equipmentOwnerId"
    )
    val traits: List<Trait>
)