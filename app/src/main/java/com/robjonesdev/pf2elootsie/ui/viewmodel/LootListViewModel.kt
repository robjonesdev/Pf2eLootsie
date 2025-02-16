package com.robjonesdev.pf2elootsie.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robjonesdev.pf2elootsie.data.datasource.EquipmentDatabase
import com.robjonesdev.pf2elootsie.data.models.Equipment
import com.robjonesdev.pf2elootsie.data.models.FilteredEquipment
import com.robjonesdev.pf2elootsie.data.models.LootList
import com.robjonesdev.pf2elootsie.ui.state.LootListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Viewmodel that exposes functionality for interacting with
 * [com.robjonesdev.pf2elootsie.data.models.LootList] objects
 * or getting updates regarding the state the UI should be in
 * with respect to loot lists the application
 * understands
 */
//TODO: should the coroutines launched by this class be captured and have cancel logic?
@HiltViewModel
class LootListViewModel @Inject constructor(
    equipmentDatabase: EquipmentDatabase,
    private val operationalDispatcher: CoroutineContext
) : ViewModel() {

    private val _uiState = MutableStateFlow(LootListUiState())
    val uiState: StateFlow<LootListUiState> get() = _uiState

    private val equipmentDao = equipmentDatabase.equipmentDao()

    fun addLootList(newLootList: LootList) {
        viewModelScope.launch(operationalDispatcher) {
            _uiState.value = _uiState.value.copy(
                lootLists = _uiState.value.lootLists + newLootList
            )
        }
    }

    fun clearLootLists() {
        viewModelScope.launch(operationalDispatcher) {
            _uiState.value = _uiState.value.copy(
                lootLists = emptyList()
            )
        }
    }

    fun generateLootList(filteredEquipment: FilteredEquipment) {
        val shuffledEquipment = filteredEquipment.filteredEquipment.shuffled()
        val selectedEquipment = mutableListOf<Equipment>()
        var totalPrice = 0f

        for (equipment in shuffledEquipment) {
            if (totalPrice + equipment.price > (filteredEquipment.generationPreferences.totalPriceLimit.toFloatOrNull() ?: 1.0f)) break
            selectedEquipment.add(equipment)
            totalPrice += equipment.price
        }

        addLootList(
            LootList(
                uniqueID = UUID.randomUUID(),
                name = filteredEquipment.generationPreferences.lootListName,
                equipment = selectedEquipment.toList(),
                generationPreferences = filteredEquipment.generationPreferences
            )
        )
    }
}