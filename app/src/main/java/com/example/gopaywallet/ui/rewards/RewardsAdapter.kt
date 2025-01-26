package com.example.gopaywallet.ui.rewards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gopaywallet.R

class RewardsAdapter : RecyclerView.Adapter<RewardsAdapter.RewardsViewHolder>() {

    private var rewardsList: List<String?> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rewards, parent, false)
        return RewardsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RewardsViewHolder, position: Int) {
        val reward = rewardsList[position]
        holder.bind(reward)
    }

    override fun getItemCount(): Int {
        return rewardsList.size
    }

    fun setRewards(rewards: List<String?>) {
        this.rewardsList = rewards
        notifyDataSetChanged()
    }

    class RewardsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val rewardTextView: TextView = itemView.findViewById(R.id.tvRewardText)

        fun bind(reward: String?) {
            rewardTextView.text = reward
        }
    }
}
