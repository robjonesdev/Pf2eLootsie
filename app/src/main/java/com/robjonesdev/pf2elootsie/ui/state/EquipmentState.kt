package com.robjonesdev.pf2elootsie.ui.state

import java.math.BigDecimal

/**
 * State data for saved equipment for the UI to consume from
 */
data class EquipmentState(
    val dbPopulationProgress: BigDecimal = BigDecimal("0.00"),
    val isRetrievingFromNetwork: Boolean = false,
    val error: Throwable? = null
)