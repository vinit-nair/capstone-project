package com.example.gopaywallet.ui.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gopaywallet.data.model.Transaction
import com.example.gopaywallet.data.model.TransactionRequest
import com.example.gopaywallet.data.model.TransactionType
import com.example.gopaywallet.data.repository.TransactionRepository
import kotlinx.coroutines.launch
import java.math.BigDecimal

class CreateTransactionViewModel(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _transactionResult = MutableLiveData<Result<Transaction>>()
    val transactionResult: LiveData<Result<Transaction>> = _transactionResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val title = MutableLiveData<String>()
    val amount = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val recipientName = MutableLiveData<String>()
    val recipientPhone = MutableLiveData<String>()

    fun createTransaction(userId: Long, type: TransactionType) {
        val titleValue = title.value
        val amountValue = amount.value

        if (titleValue.isNullOrBlank() || amountValue.isNullOrBlank()) {
            _transactionResult.value = Result.failure(Exception("Title and amount are required"))
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val request = TransactionRequest(
                    title = titleValue,
                    amount = BigDecimal(amountValue),
                    type = type,
                    description = description.value,
                    recipientName = recipientName.value,
                    recipientPhone = recipientPhone.value
                )
                val transaction = transactionRepository.createTransaction(userId, request)
                _transactionResult.value = Result.success(transaction)
            } catch (e: Exception) {
                _transactionResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
} 