package edu.bluejack20_2.chantuy.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
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
import edu.bluejack20_2.chantuy.repositories.CurhatCommentRepository
import edu.bluejack20_2.chantuy.repositories.CurhatRepository
import edu.bluejack20_2.chantuy.repositories.UserRepository
import edu.bluejack20_2.chantuy.utils.CurhatViewUtil
import edu.bluejack20_2.chantuy.views.curhat_detail.CurhatDetailActivity

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
            setOnViewMoreListener(curhat.id)

            UserRepository.getUserById(curhat.user) { user ->
                binding.curhatCardUsername.text = if (curhat.isAnonymous) "Anonymous" else user?.name
            }

            CurhatCommentRepository.getCommentCount(curhat.id) { count ->
                binding.curhatCardCommentCount.text = count.toString()
            }


            setReactionBtnColor(curhat)
            setLikePopupMenu(curhat.id)
            setDislikePopupMenu(curhat.id)
        }

        private fun setReactionBtnColor(curhat: Curhat) {
            if (curhat == null) return
            val userId = UserRepository.getCurrentUserId()

            if (
                curhat.usersGiveThumbUp?.contains(userId)!! ||
                curhat.usersGiveCool?.contains(userId)!! ||
                curhat.usersGiveLove?.contains(userId)!!
            ) {
                binding.curhatCardThumbUpBtn.setColorFilter(
                    ContextCompat.getColor(binding.root.context, R.color.green),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                )
            }

            if (
                curhat.usersGiveThumbDowns?.contains(userId)!! ||
                curhat.usersGiveAngry?.contains(userId)!!
            ) {
                binding.curhatCardThumbDownBtn.setColorFilter(
                    ContextCompat.getColor(binding.root.context, R.color.dark_red),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                )
            }
        }

        @SuppressLint("RestrictedApi")
        private fun setDislikePopupMenu(id: String) {
            binding.curhatCardThumbDownBtn.setOnClickListener {
                val popupMenu = PopupMenu(binding.root.context, it)
                popupMenu.inflate(R.menu.dislike_curhat_menu_items)

                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.dislike_curhat_thumb_down -> {
                            CurhatRepository.addDislikeReaction(id, CurhatReaction.THUMB_DOWN) {
                                CurhatViewUtil.changeReactionBtnColor(
                                    binding.curhatCardThumbDownBtn, R.color.dark_red, binding.root
                                )
                            }
                            true
                        }
                        R.id.dislike_curhat_angry -> {
                            CurhatRepository.addDislikeReaction(id, CurhatReaction.ANGRY) {
                                CurhatViewUtil.changeReactionBtnColor(
                                    binding.curhatCardThumbDownBtn, R.color.dark_red, binding.root
                                )
                            }
                            true
                        }
                        else -> false
                    }
                }

                val popupHelper = MenuPopupHelper(binding.root.context, popupMenu.menu as MenuBuilder, it)
                popupHelper.setForceShowIcon(true)
                popupHelper.show()
            }
        }

        @SuppressLint("RestrictedApi")
        private fun setLikePopupMenu(id: String) {
            binding.curhatCardThumbUpBtn.setOnClickListener {
                val popupMenu = PopupMenu(binding.root.context, it)
                popupMenu.inflate(R.menu.like_curhat_menu_items)

                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.like_curhat_thumb_up -> {
                            CurhatRepository.addLikeReaction(id, CurhatReaction.THUMB_UP) {
                                CurhatViewUtil.changeReactionBtnColor(
                                    binding.curhatCardThumbUpBtn, R.color.green, binding.root
                                )
                            }
                            true
                        }
                        R.id.like_curhat_cool -> {
                            CurhatRepository.addLikeReaction(id, CurhatReaction.COOL) {
                                CurhatViewUtil.changeReactionBtnColor(
                                    binding.curhatCardThumbUpBtn, R.color.green, binding.root
                                )
                            }
                            true
                        }
                        R.id.like_curhat_love -> {
                            CurhatRepository.addLikeReaction(id, CurhatReaction.LOVE) {
                                CurhatViewUtil.changeReactionBtnColor(
                                    binding.curhatCardThumbUpBtn, R.color.green, binding.root
                                )
                            }
                            true
                        }

                        else -> false
                    }
                }

                val popupHelper = MenuPopupHelper(binding.root.context, popupMenu.menu as MenuBuilder, it)
                popupHelper.setForceShowIcon(true)
                popupHelper.show()
            }
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

