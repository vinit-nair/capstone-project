package com.example.gopaywallet.ui.rewards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gopaywallet.data.SessionManager
import com.example.gopaywallet.data.repository.RewardsRepository
import com.example.gopaywallet.di.NetworkModule
import com.example.gopaywallet.ui.home.HomeViewModel

class RewardsViewModelFactory(
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RewardsViewModel(
                RewardsRepository(
                    NetworkModule.rewardsApi,
                    sessionManager
                ),
                sessionManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}