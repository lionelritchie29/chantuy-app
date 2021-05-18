package edu.bluejack20_2.chantuy.views

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.databinding.CurhatCardItemBinding
import edu.bluejack20_2.chantuy.databinding.CurhatInfoPopupBinding
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.models.CurhatComment
import edu.bluejack20_2.chantuy.models.CurhatReaction
import edu.bluejack20_2.chantuy.models.User
import edu.bluejack20_2.chantuy.repositories.CurhatCommentRepository
import edu.bluejack20_2.chantuy.repositories.CurhatRepository
import edu.bluejack20_2.chantuy.repositories.UserRepository
import edu.bluejack20_2.chantuy.utils.CurhatUtil
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
            binding.curhatCardLikeCount.text = curhat.likeCount.toString()
            binding.curhatCardDislikeCount.text = curhat.dislikeCount.toString()
            setOnViewMoreListener(curhat.id)

            UserRepository.getUserById(curhat.user) { user ->
                binding.curhatCardUsername.text = if (curhat.isAnonymous) "Anonymous" else user?.name
                CurhatViewUtil.setCurhatUserImage(curhat.isAnonymous, user!!, binding.curhatCardUserimage, binding.root)
            }

            CurhatViewUtil.setReactionBtnColor(
                binding.curhatCardThumbUpBtn,
                binding.curhatCardThumbDownBtn,
                curhat, binding.root)
            CurhatViewUtil.setLikePopupMenu(binding.curhatCardThumbUpBtn, binding.curhatCardThumbDownBtn, curhat, binding.root) {
                updateLikeDislikeCount(curhat.id)
            }
            CurhatViewUtil.setDislikePopupMenu(binding.curhatCardThumbUpBtn, binding.curhatCardThumbDownBtn, curhat, binding.root) {
                updateLikeDislikeCount(curhat.id)
            }

            binding.curhatCardInfoBtn.setOnClickListener {
                CurhatViewUtil.showCurhatInfoModal(curhat, binding.root.context)
            }
        }

        private fun updateLikeDislikeCount(curhatId: String) {
            CurhatRepository.getLikeDislikeCount(curhatId) { likeCount: Long, dislikeCount: Long ->

                binding.curhatCardLikeCount.text = likeCount.toString()
                binding.curhatCardDislikeCount.text = dislikeCount.toString()
            }
        }

        private fun setOnViewMoreListener(id: String) {
            binding.curhatCardViewBtn.setOnClickListener {
                CurhatUtil.moveToCurhatDetail(id, binding.root.context)
            }
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

