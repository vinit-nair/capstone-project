package com.example.gopaywallet.ui.rewards

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gopaywallet.R
import com.example.gopaywallet.data.SessionManager
import com.example.gopaywallet.data.repository.RewardsRepository
import com.example.gopaywallet.databinding.FragmentRewardsBinding
import com.example.gopaywallet.databinding.FragmentTransactionsBinding
import com.example.gopaywallet.utils.showToast

class RewardsFragment : Fragment() {

    private lateinit var rewardsViewModel: RewardsViewModel
    private lateinit var binding: FragmentRewardsBinding
    private lateinit var rewardsAdapter: RewardsAdapter
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRewardsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI components
        val rewardPointsTextView: TextView = view.findViewById(R.id.tvRewardPoints)
        val rewardsRecyclerView: RecyclerView = view.findViewById(R.id.rvRewards)

        // Initialize RecyclerView and Adapter
        rewardsAdapter = RewardsAdapter()
        rewardsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rewardsAdapter
        }

        // Initialize ViewModel
        val sessionManager = SessionManager(requireContext())
        val viewModelFactory = RewardsViewModelFactory(sessionManager)
        rewardsViewModel = ViewModelProvider(this, viewModelFactory)[RewardsViewModel::class.java]

        // Observe ViewModel LiveData
        rewardsViewModel.rewards.observe(viewLifecycleOwner, Observer { rewards ->
            rewardPointsTextView.text = rewards.points.toString()
            rewardsAdapter.setRewards(rewards.rewardsList.toList())
        })

        rewardsViewModel.isLoading.observe(viewLifecycleOwner, Observer { loading ->
            isLoading = loading
            //binding.swipeRefresh.isRefreshing = loading
        })

        rewardsViewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            requireContext().showToast(errorMessage)
        })
    }
}
