package com.example.gopaywallet.data.model

import java.math.BigDecimal

data class Rewards(
    val points: BigDecimal,
    val rewards: Array<String?>
)
