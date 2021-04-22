package edu.bluejack20_2.chantuy.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.databinding.CurhatCardItemBinding
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.models.CurhatReaction
import edu.bluejack20_2.chantuy.models.User
import edu.bluejack20_2.chantuy.repositories.CurhatCommentRepository
import edu.bluejack20_2.chantuy.repositories.CurhatRepository
import edu.bluejack20_2.chantuy.repositories.UserRepository
import edu.bluejack20_2.chantuy.utils.CurhatViewUtil
import edu.bluejack20_2.chantuy.views.curhat_detail.CurhatDetailActivity
import kotlin.random.Random

class CurhatAdapter() : ListAdapter<Curhat, CurhatAdapter.ViewHolder>(CurhatDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val curhat = getItem(position)
        holder.bind(curhat)
    }

    class ViewHolder(var binding: CurhatCardItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(curhat: Curhat) {
            binding.curhatCardContent.text = curhat.content
            binding.curhatCardDate.text = CurhatViewUtil.formatDate(curhat.createdAt)
            CurhatRepository.incrementViewCount(curhat.id)
            binding.curhatCardCommentCount.text = curhat.commentCount.toString()
            setOnViewMoreListener(curhat.id)

            UserRepository.getUserById(curhat.user) { user ->
                binding.curhatCardUsername.text = if (curhat.isAnonymous) "Anonymous" else user?.name
                CurhatViewUtil.setCurhatUserImage(curhat.isAnonymous, user!!, binding.curhatCardUserimage, binding.root)
            }

            CurhatViewUtil.setReactionBtnColor(
                binding.curhatCardThumbUpBtn,
                binding.curhatCardThumbDownBtn,
                curhat, binding.root)
            CurhatViewUtil.setLikePopupMenu(binding.curhatCardThumbUpBtn, binding.curhatCardThumbDownBtn, curhat, binding.root)
            CurhatViewUtil.setDislikePopupMenu(binding.curhatCardThumbUpBtn, binding.curhatCardThumbDownBtn, curhat, binding.root)
        }


        private fun setOnViewMoreListener(id: String) {
            binding.curhatCardViewBtn.setOnClickListener {
                moveToCurhatDetail(id)
            }
        }

        private fun moveToCurhatDetail(id: String) {
            val intent = Intent(binding.root.context, CurhatDetailActivity::class.java)
            val b = Bundle()
            b.putString("id", id);
            intent.putExtras(b)
            binding.root.context.startActivity(intent)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CurhatCardItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

object CurhatDiffCallback : DiffUtil.ItemCallback<Curhat>() {
    override fun areItemsTheSame(oldItem: Curhat, newItem: Curhat): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Curhat, newItem: Curhat): Boolean {
        return oldItem.id == newItem.id
    }
}

