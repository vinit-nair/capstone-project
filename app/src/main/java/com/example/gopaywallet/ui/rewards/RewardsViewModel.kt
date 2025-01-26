package com.example.gopaywallet.ui.rewards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gopaywallet.data.SessionManager
import com.example.gopaywallet.data.model.Rewards
import com.example.gopaywallet.data.model.Transaction
import com.example.gopaywallet.data.repository.RewardsRepository
import kotlinx.coroutines.launch

class RewardsViewModel(
    private val rewardsRepository: RewardsRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _rewards = MutableLiveData<Rewards>()
    val rewards: LiveData<Rewards> = _rewards

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        loadRewards()
    }

    private fun loadRewards() {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = sessionManager.getUserId()
            println("Loading rewards for user: $userId")
            val newRewards = rewardsRepository.getUserRewards(userId)
            println("Received rewards: ${newRewards}")
            _rewards.value = newRewards
            _isLoading.value = false
        }
    }

}