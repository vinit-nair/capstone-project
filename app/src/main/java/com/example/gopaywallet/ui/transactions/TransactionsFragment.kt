package com.example.gopaywallet.ui.transactions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gopaywallet.data.SessionManager
import com.example.gopaywallet.data.repository.TransactionRepository
import com.example.gopaywallet.databinding.FragmentTransactionsBinding
import com.example.gopaywallet.di.NetworkModule
import com.example.gopaywallet.ui.home.TransactionAdapter
import com.example.gopaywallet.utils.showToast
import com.example.gopaywallet.ui.common.TransactionItemDecoration

class TransactionsFragment : Fragment() {
    private lateinit var binding: FragmentTransactionsBinding
    private val viewModel: TransactionsViewModel by viewModels {
        TransactionsViewModelFactory(SessionManager(requireContext()))
    }
    private val transactionAdapter = TransactionAdapter()
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("TransactionsFragment: onViewCreated")
        setupRecyclerView()
        setupSwipeRefresh()
        setupFab()
        setupObservers()
        
        // Force refresh when fragment becomes visible
        viewModel.refreshTransactions()
    }

    private fun setupRecyclerView() {
        println("TransactionsFragment: setupRecyclerView")
        binding.rvTransactions.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = transactionAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            
            // Debug visibility
            println("RecyclerView visibility: ${visibility == View.VISIBLE}")
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshTransactions()
        }
    }

    private fun setupFab() {
        binding.fabNewTransaction.setOnClickListener {
            startActivityForResult(
                Intent(requireContext(), CreateTransactionActivity::class.java),
                CreateTransactionActivity.REQUEST_CODE
            )
        }
    }

    private fun setupObservers() {
        viewModel.transactions.observe(viewLifecycleOwner) { transactions ->
            println("TransactionsFragment: Received ${transactions.size} transactions")
            transactions.forEach { 
                println("Transaction: ${it.title} - ${it.amount}")
            }
            
            binding.rvTransactions.post {
                transactionAdapter.submitList(transactions.toList()) {
                    println("TransactionsFragment: List submitted to adapter")
                    binding.emptyView.visibility = if (transactions.isEmpty()) View.VISIBLE else View.GONE
                    binding.rvTransactions.visibility = if (transactions.isEmpty()) View.GONE else View.VISIBLE
                    println("RecyclerView visibility after update: ${binding.rvTransactions.visibility == View.VISIBLE}")
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            isLoading = loading
            binding.swipeRefresh.isRefreshing = loading
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            requireContext().showToast(errorMessage)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CreateTransactionActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            viewModel.refreshTransactions()
        }
    }

    override fun onResume() {
        super.onResume()
        println("TransactionsFragment: onResume")
        viewModel.refreshTransactions()
    }
} 