package com.example.gopaywallet.data.api

import com.example.gopaywallet.data.model.Rewards
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RewardsApi {
    @GET("api/rewards")
    suspend fun getUserRewards(
        @Query("userId") userId: Long,
        @Header("Authorization") token: String,
        @Header("X-User-Id") currentUserId: Long
    ): Rewards
}