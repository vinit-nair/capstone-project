package com.example.gopaywallet.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gopaywallet.R
import com.example.gopaywallet.data.SessionManager
import com.example.gopaywallet.data.repository.TransactionRepository
import com.example.gopaywallet.databinding.ActivityHomeBinding
import com.example.gopaywallet.di.NetworkModule
import com.example.gopaywallet.ui.rewards.RewardsFragment
import com.example.gopaywallet.ui.spotcash.SpotCashActivity
import com.example.gopaywallet.utils.showToast
import com.example.gopaywallet.ui.transactions.CreateTransactionActivity
import com.example.gopaywallet.ui.transactions.TransactionsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private val transactionAdapter = TransactionAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupToolbar()
        setupBottomNavigation()
        setupClickListeners()
        setupObservers()
        setupRecyclerView()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "GoPay Wallet"
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Show home content
                    binding.contentContainer.visibility = View.VISIBLE
                    binding.fragmentContainer.visibility = View.GONE
                    supportFragmentManager.findFragmentById(R.id.fragmentContainer)?.let {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .hide(it)
                            .commit()
                    }
                    true
                }
                R.id.navigation_transactions -> {
                    // Show transactions fragment
                    binding.contentContainer.visibility = View.GONE
                    binding.fragmentContainer.visibility = View.VISIBLE
                    var fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
                    if (fragment == null) {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .add(R.id.fragmentContainer, TransactionsFragment())
                            .commit()
                    } else {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.fragmentContainer, TransactionsFragment())
                            .commit()
                    }
                    true
                }
                R.id.navigation_rewards -> {
                    // Rewards fragment
                    binding.contentContainer.visibility = View.GONE
                    binding.fragmentContainer.visibility = View.VISIBLE
                    var fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
                    if (fragment == null) {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .add(R.id.fragmentContainer, RewardsFragment())
                            .commit()
                    } else {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.fragmentContainer, RewardsFragment())
                            .commit()
                    }
                    true
                }
                R.id.navigation_profile -> {
                    // Handle profile navigation
                    true
                }
                else -> false
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnSendMoney.setOnClickListener {
            startActivityForResult(
                Intent(this, CreateTransactionActivity::class.java),
                CreateTransactionActivity.REQUEST_CODE
            )
        }

        binding.btnRequestMoney.setOnClickListener {
            // Handle request money
        }

        binding.btnSpotCash.setOnClickListener {
            val intent = Intent(this, SpotCashActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupObservers() {
        viewModel.balance.observe(this) { balance ->
            binding.tvBalance.text = getString(R.string.balance_format, balance)
        }

        viewModel.transactions.observe(this) { transactions ->
            // Update RecyclerView
            binding.rvTransactions.adapter?.let { adapter ->
                if (adapter is TransactionAdapter) {
                    adapter.submitList(transactions)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvTransactions.apply {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(this@HomeActivity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_notifications -> {
                showToast("Notifications clicked")
                true
            }
            R.id.action_settings -> {
                showToast("Settings clicked")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshTransactions()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CreateTransactionActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            viewModel.refreshTransactions()
            
            // Also refresh the transactions fragment if it's visible
            supportFragmentManager.findFragmentById(R.id.fragmentContainer)?.let { fragment ->
                if (fragment is TransactionsFragment && fragment.isVisible) {
                    fragment.refreshTransactions()
                }
            }
        }
    }
} 