package com.robjonesdev.pf2elootsie.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.robjonesdev.pf2elootsie.ui.navigation.LootsieNavigation
import com.robjonesdev.pf2elootsie.ui.theme.LootsieTheme
import com.robjonesdev.pf2elootsie.ui.viewmodel.EquipmentViewModel
import com.robjonesdev.pf2elootsie.ui.viewmodel.LootListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Base application class for the Pf2e Lootsie Android application
 * responsible for standing up initial composition and navigation
 */
@AndroidEntryPoint
class ApplicationActivity() : ComponentActivity() {

    constructor(
        equipmentCollectionDispatcher: CoroutineContext
    ):this() {
        this.equipmentCollectionDispatcher = equipmentCollectionDispatcher
    }

    private val equipmentViewModel: EquipmentViewModel by viewModels()
    private val lootListViewModel: LootListViewModel by viewModels()

    private var filteredEquipmentJob: Job? = null
    private var equipmentCollectionDispatcher: CoroutineContext? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        equipmentViewModel.storeEquipmentAssetsInDB()

        setContent {
            LootsieTheme {
                LootsieNavigation(
                    onPreferencesSelected = { lootGenerationPreferences ->
                        equipmentViewModel.getFilteredEquipment(
                            preferences = lootGenerationPreferences
                        )
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        filteredEquipmentJob =
            this.lifecycleScope.launch(
                equipmentCollectionDispatcher ?: Dispatchers.Default) {
                while(isActive){
                    collectedLatestFilteredEquipment()
                }
            }

    }

    override fun onPause() {
        super.onPause()
        filteredEquipmentJob?.cancel()
    }

    private suspend fun collectedLatestFilteredEquipment() {
        equipmentViewModel.generatedEquipmentFlow.collect { filteredEquipment ->
            lootListViewModel.generateLootList(filteredEquipment)
        }
    }

}

