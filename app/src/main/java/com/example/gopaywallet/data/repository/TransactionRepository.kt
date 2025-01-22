package com.example.gopaywallet.data.repository

import com.example.gopaywallet.data.api.TransactionApi
import com.example.gopaywallet.data.model.Transaction
import com.example.gopaywallet.data.model.TransactionRequest
import com.example.gopaywallet.data.SessionManager

class TransactionRepository(
    private val api: TransactionApi,
    private val sessionManager: SessionManager
) {

    suspend fun getUserTransactions(userId: Long, page: Int, pageSize: Int): List<Transaction> {
        return try {
            val token = sessionManager.getAuthToken()
            val currentUserId = sessionManager.getUserId()
            
            println("Fetching transactions for userId: $userId, currentUserId: $currentUserId")
            
            // Only fetch transactions if the requested userId matches the logged-in user
            if (userId != currentUserId) {
                println("Unauthorized attempt to access transactions for user $userId by user $currentUserId")
                return emptyList()
            }
            
            val response = api.getUserTransactions(
                userId = userId,
                page = page,
                pageSize = pageSize,
                token = token.toString(),
                currentUserId = currentUserId
            )
            
            println("Received transactions: ${response.transactions.size}")
            
            // Return all transactions from response as they're already filtered by userId on the server
            response.transactions
        } catch (e: Exception) {
            println("Error fetching transactions: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun createTransaction(userId: Long, request: TransactionRequest): Transaction {
        val token = sessionManager.getAuthToken()
        val currentUserId = sessionManager.getUserId()
        
        // Verify user is creating transaction for themselves
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
} 