package com.example.gopaywallet.data.repository

import com.example.gopaywallet.data.api.TransactionApi
import com.example.gopaywallet.data.model.Transaction
import com.example.gopaywallet.data.model.TransactionRequest
import com.example.gopaywallet.data.SessionManager

interface TransactionRepository {
    suspend fun getUserTransactions(userId: Long, page: Int, size: Int): List<Transaction>
    suspend fun createTransaction(userId: Long, request: TransactionRequest): Transaction
    suspend fun getTransactionById(id: Long): Transaction
}

