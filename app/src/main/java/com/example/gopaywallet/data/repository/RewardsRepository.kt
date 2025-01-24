package com.example.gopaywallet.data.repository

import com.example.gopaywallet.data.SessionManager
import com.example.gopaywallet.data.api.RewardsApi
import com.example.gopaywallet.data.model.Rewards

class RewardsRepository (
    private val api: RewardsApi,
    private val sessionManager: SessionManager
) {
    suspend fun getUserRewards(userId: Long): Rewards {

        val token = sessionManager.getAuthToken()
        val currentUserId = sessionManager.getUserId()
        println("Fetching rewards for userId: $userId, currentUserId: $currentUserId")

        val response = api.getUserRewards(
            userId = userId,
            token = token.toString(),
            currentUserId = currentUserId
        )
        println("Received transactions: ${response.rewards.size}")
        return response
    }
}