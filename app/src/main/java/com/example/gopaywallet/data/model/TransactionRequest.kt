package com.example.gopaywallet.data.model

import java.math.BigDecimal

data class TransactionRequest(
    val title: String,
    val amount: BigDecimal,
    val type: TransactionType,
    val description: String?,
    val recipientName: String?,
    val recipientPhone: String?
) 