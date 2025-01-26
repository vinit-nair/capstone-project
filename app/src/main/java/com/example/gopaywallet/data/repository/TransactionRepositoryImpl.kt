package com.example.gopaywallet.data.repository

import com.example.gopaywallet.data.SessionManager
import com.example.gopaywallet.data.api.TransactionApi
import com.example.gopaywallet.data.model.Transaction
import com.example.gopaywallet.data.model.TransactionRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val api: TransactionApi,
    private val sessionManager: SessionManager
) : TransactionRepository {

    override suspend fun getUserTransactions(userId: Long, page: Int, pageSize: Int): List<Transaction> {
        return try {
            val token = sessionManager.getAuthToken()
            val currentUserId = sessionManager.getUserId()
            
            if (userId != currentUserId) {
                println("Unauthorized attempt to access transactions")
                return emptyList()
            }
            
            val response = api.getUserTransactions(
                userId = userId,
                page = page,
                pageSize = pageSize,
                token = token.toString(),
                currentUserId = currentUserId
            )
            response.transactions
        } catch (e: Exception) {
            println("Error fetching transactions: ${e.message}")
            emptyList()
        }
    }

    override suspend fun createTransaction(userId: Long, request: TransactionRequest): Transaction {
        val token = sessionManager.getAuthToken()
        val currentUserId = sessionManager.getUserId()
        
        if (userId != currentUserId) {
            throw IllegalArgumentException("Cannot create transaction for another user")
        }
        
        return api.createTransaction(
            userId = userId,
            request = request,
            token = token.toString(),
            currentUserId = currentUserId
        )
    }

    override suspend fun getTransactionById(id: Long): Transaction {
        throw UnsupportedOperationException("Method not implemented")
    }
} 