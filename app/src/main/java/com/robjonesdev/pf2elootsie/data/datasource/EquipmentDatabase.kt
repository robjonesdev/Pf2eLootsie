package com.robjonesdev.pf2elootsie.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.robjonesdev.pf2elootsie.data.models.Equipment
import com.robjonesdev.pf2elootsie.data.models.Trait

@Database(
    entities = [Equipment::class, Trait::class],
    version = 1,
    exportSchema = false
)
abstract class EquipmentDatabase : RoomDatabase() {
    abstract fun equipmentDao(): EquipmentDao
}