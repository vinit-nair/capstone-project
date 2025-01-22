package com.example.gopaywallet.data.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class Transaction(
    val id: Long,
    val userId: Long,
    val title: String,
    val amount: BigDecimal,
    val dateTime: LocalDateTime,
    val type: TransactionType,
    val description: String?,
    val recipientName: String?,
    val recipientPhone: String?
)

enum class TransactionType {
    SEND, RECEIVE, SPOT_CASH
} 