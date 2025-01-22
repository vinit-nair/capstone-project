package com.example.gopaywallet.data.api

import com.example.gopaywallet.data.model.Transaction
import com.example.gopaywallet.data.model.TransactionRequest
import com.example.gopaywallet.data.model.TransactionResponse
import retrofit2.http.*

interface TransactionApi {
    @GET("api/transactions")
    suspend fun getUserTransactions(
        @Query("userId") userId: Long,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Header("Authorization") token: String,
        @Header("X-User-Id") currentUserId: Long
    ): TransactionResponse

    @POST("api/transactions")
    suspend fun createTransaction(
        @Query("userId") userId: Long,
        @Body request: TransactionRequest,
        @Header("Authorization") token: String,
        @Header("X-User-Id") currentUserId: Long
    ): Transaction
} 