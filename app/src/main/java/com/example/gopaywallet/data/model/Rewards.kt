package com.example.gopaywallet.data.model

import java.math.BigDecimal

data class Rewards(
    val points: BigDecimal,
    val rewardsList: List<String?>
)
