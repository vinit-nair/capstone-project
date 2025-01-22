package com.example.gopaywallet.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gopaywallet.data.SessionManager
import com.example.gopaywallet.data.repository.TransactionRepository
import com.example.gopaywallet.di.NetworkModule

class HomeViewModelFactory(
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(
                TransactionRepository(
                    NetworkModule.transactionApi,
                    sessionManager
                ),
                sessionManager
            ) as T
    }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 