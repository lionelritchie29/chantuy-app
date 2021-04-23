package edu.bluejack20_2.chantuy.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.chantuy.databinding.FeedbackCardItemBinding
import edu.bluejack20_2.chantuy.models.Feedback
import edu.bluejack20_2.chantuy.repositories.UserRepository
import edu.bluejack20_2.chantuy.utils.CurhatViewUtil

class FeedbackAdapter : ListAdapter<Feedback, FeedbackAdapter.ViewHolder>(FeedbackDiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder (val binding: FeedbackCardItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(feedback: Feedback) {
            binding.feedbackContent.text = feedback.content
            binding.feedbackDate.text = CurhatViewUtil.formatDate(feedback.createdAt)

            UserRepository.getUserById(feedback.userId) {
                if (it != null) {
                    binding.feedbackUserName.text = it.name
                }
            }

            setStatusChip(feedback.status)
        }

        fun setStatusChip(status: String?) {
            if (status == "SOLVED") {
                binding.feedbackStatusChipPending.visibility = View.GONE
                binding.feedbackStatusChipSolved.visibility = View.VISIBLE
                binding.feedbackMarkSolvedBtn.visibility = View.INVISIBLE
            } else {
                binding.feedbackStatusChipPending.visibility = View.VISIBLE
                binding.feedbackStatusChipSolved.visibility = View.GONE
                binding.feedbackMarkSolvedBtn.visibility = View.VISIBLE
            }
        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FeedbackCardItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

object FeedbackDiffCallback : DiffUtil.ItemCallback<Feedback>() {
    override fun areItemsTheSame(oldItem: Feedback, newItem: Feedback): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Feedback, newItem: Feedback): Boolean {
        return oldItem.id == newItem.id
    }
}