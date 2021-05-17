package edu.bluejack20_2.chantuy.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.chantuy.databinding.ProfilePostedCurhatCardItemBinding
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.utils.CurhatUtil
import edu.bluejack20_2.chantuy.utils.CurhatViewUtil

class ProfileCurhatPostedAdapter(): ListAdapter<Curhat, ProfileCurhatPostedAdapter.ViewHolder>(ProfileCurhatPostedDiffCallback) {

    class ViewHolder(val binding: ProfilePostedCurhatCardItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(curhat: Curhat) {
            binding.curhatCardContent.text = curhat.content
            binding.curhatCardDate.text = CurhatViewUtil.formatDate(curhat.createdAt)
            binding.curhatCardLikeCount.text = curhat.likeCount.toString()
            binding.curhatCardDislikeCount.text = curhat.dislikeCount.toString()

            binding.curhatCardInfoBtn.setOnClickListener {
                CurhatViewUtil.showCurhatInfoModal(curhat, binding.root.context)
            }

            binding.curhatCardViewBtn.setOnClickListener {
                CurhatUtil.moveToCurhatDetail(curhat.id, binding.root.context)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProfilePostedCurhatCardItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val curhat = getItem(position)
        holder.bind(curhat)
    }
}

object ProfileCurhatPostedDiffCallback : DiffUtil.ItemCallback<Curhat>() {
    override fun areItemsTheSame(oldItem: Curhat, newItem: Curhat): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Curhat, newItem: Curhat): Boolean {
        return oldItem.id == newItem.id
    }
}