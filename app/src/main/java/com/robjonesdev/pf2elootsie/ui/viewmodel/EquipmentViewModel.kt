package com.robjonesdev.pf2elootsie.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.robjonesdev.pf2elootsie.data.datasource.EquipmentDatabase
import com.robjonesdev.pf2elootsie.data.models.Equipment
import com.robjonesdev.pf2elootsie.data.models.EquipmentTraits
import com.robjonesdev.pf2elootsie.data.models.FilteredEquipment
import com.robjonesdev.pf2elootsie.data.models.LootGenerationPreferences
import com.robjonesdev.pf2elootsie.data.workers.JsonEquipmentDatabaseBuilderWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class EquipmentViewModel @Inject constructor(
    equipmentDatabase: EquipmentDatabase,
    private val workManager: WorkManager,
    private val generationDispatcher: CoroutineContext = Dispatchers.Default
) : ViewModel() {

    private val equipmentDao = equipmentDatabase.equipmentDao()

    private val _generatedEquipmentFlow = MutableSharedFlow<FilteredEquipment>(replay = 0)
    val generatedEquipmentFlow: Flow<FilteredEquipment>
        get() {
            return _generatedEquipmentFlow
        }

    fun storeEquipmentAssetsInDB() {
        val constraints = Constraints.Builder()
           // .setRequiresBatteryNotLow(true)
           // .setRequiresStorageNotLow(true)
            .build()

        val myWorkRequest: WorkRequest =
            OneTimeWorkRequestBuilder<JsonEquipmentDatabaseBuilderWorker>()
                .setConstraints(constraints)
                .addTag("Initial Data Migration")
                .build()

        workManager.enqueue(myWorkRequest)
    }

    fun getFilteredEquipment(
        preferences: LootGenerationPreferences
    ) {
        viewModelScope.launch(generationDispatcher) {
            val rarityStrings = preferences.rarities.map { it.name.lowercase() }
            val traitStrings = preferences.traits.map { it.name.lowercase() }

            val equipmentWithTraits = equipmentDao.getFilteredEquipmentWithTraits(
                targetLootLevel = preferences.targetLootLevel.toIntOrNull() ?: 1,
                totalPriceLimit = preferences.totalPriceLimit.toFloatOrNull() ?: 50.0f,
                rarities = rarityStrings,
                traits = traitStrings,
            )

            val equipmentList = mutableListOf<Equipment>()
            for(equipmentWithTrait in equipmentWithTraits) {
                val equipment = equipmentWithTrait.equipment.copy()
                equipment.traits = equipmentWithTrait.traits.map { it.name }
                equipmentList.add(
                    equipment
                )
            }

            _generatedEquipmentFlow.emit(
                FilteredEquipment(
                    filteredEquipment = equipmentList,
                    generationPreferences = preferences
                )
            )
        }
    }
}
