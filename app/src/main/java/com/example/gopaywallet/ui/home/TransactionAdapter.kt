package com.example.gopaywallet.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gopaywallet.R
import com.example.gopaywallet.data.model.Transaction
import com.example.gopaywallet.data.model.TransactionType
import com.example.gopaywallet.databinding.ItemTransactionBinding
import java.time.format.DateTimeFormatter
import java.util.Locale

class TransactionAdapter : ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(
    TransactionDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = getItem(position)
        println("Binding transaction at position $position: ${transaction.title}") // Debug log
        holder.bind(transaction)
    }

    class TransactionViewHolder(private val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("NewApi")
        private val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a", Locale.getDefault())

        @SuppressLint("NewApi")
        fun bind(transaction: Transaction) {
            println("Binding transaction: ${transaction.title}") // Debug log
            binding.tvTransactionTitle.text = transaction.title
            binding.tvDateTime.text = transaction.dateTime.format(dateFormatter)
            
            // Format amount with + or - prefix
            val amountPrefix = if (transaction.type == TransactionType.RECEIVE) "+" else "-"
            binding.tvAmount.text = String.format("%s$%s", amountPrefix, transaction.amount.toPlainString())
            
            // Set text color based on transaction type
            binding.tvAmount.setTextColor(
                binding.root.context.getColor(
                    if (transaction.type == TransactionType.RECEIVE) 
                        R.color.transaction_positive 
                    else 
                        R.color.transaction_negative
                )
            )
        }
    }
}

class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
    }
} 