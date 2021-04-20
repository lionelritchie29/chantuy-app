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
import android.widget.TextView
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.chantuy.R
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

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        private val content: TextView = view.findViewById(R.id.curhat_card_content)
        private val username: TextView = view.findViewById(R.id.curhat_card_username)
        private val postedDate: TextView = view.findViewById(R.id.curhat_card_date)
        private val viewMoreBtn: Button = view.findViewById(R.id.curhat_card_view_btn)
        private val commentCount: TextView = view.findViewById(R.id.curhat_card_comment_count)
        private val likeBtn: ImageButton = view.findViewById(R.id.curhat_card_thumb_up_btn)
        private val dislikeBtn: ImageButton = view.findViewById(R.id.curhat_card_thumb_down_btn)

        fun bind(curhat: Curhat) {
            content.text = curhat.content
            postedDate.text = CurhatViewUtil.formatDate(curhat.createdAt)
            CurhatRepository.incrementViewCount(curhat.id)
            setOnViewMoreListener(curhat.id)

            UserRepository.getUserById(curhat.user) { user ->
                username.text = if (curhat.isAnonymous) "Anonymous" else user?.name
            }

            CurhatCommentRepository.getCommentCount(curhat.id) { count ->
                commentCount.text = count.toString()
            }


            setReactionBtnColor(curhat)
            setLikePopupMenu(curhat.id)
            setDislikePopupMenu(curhat.id)
        }

        private fun setReactionBtnColor(curhat: Curhat) {
            if (curhat == null) return
            val userId = UserRepository.getCurrentUserId()
            Log.i("CurhatAdapter", curhat.toString())

            if (
                curhat.usersGiveThumbUp?.contains(userId)!! ||
                curhat.usersGiveCool?.contains(userId)!! ||
                curhat.usersGiveLove?.contains(userId)!!
            ) {
                likeBtn.setColorFilter(
                    ContextCompat.getColor(view.context, R.color.green),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                )
            }

            if (
                curhat.usersGiveThumbDowns?.contains(userId)!! ||
                curhat.usersGiveAngry?.contains(userId)!!
            ) {
                dislikeBtn.setColorFilter(
                    ContextCompat.getColor(view.context, R.color.dark_red),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                )
            }
        }

        @SuppressLint("RestrictedApi")
        private fun setDislikePopupMenu(id: String) {
            dislikeBtn.setOnClickListener {
                val popupMenu = PopupMenu(view.context, it)
                popupMenu.inflate(R.menu.dislike_curhat_menu_items)

                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.dislike_curhat_thumb_down -> {
                            CurhatRepository.addDislikeReaction(id, CurhatReaction.THUMB_DOWN) {
                                CurhatViewUtil.changeReactionBtnColor(
                                    dislikeBtn, R.color.dark_red, view
                                )
                            }
                            true
                        }
                        R.id.dislike_curhat_angry -> {
                            CurhatRepository.addDislikeReaction(id, CurhatReaction.ANGRY) {
                                CurhatViewUtil.changeReactionBtnColor(
                                    dislikeBtn, R.color.dark_red, view
                                )
                            }
                            true
                        }
                        else -> false
                    }
                }

                val popupHelper = MenuPopupHelper(view.context, popupMenu.menu as MenuBuilder, it)
                popupHelper.setForceShowIcon(true)
                popupHelper.show()
            }
        }

        @SuppressLint("RestrictedApi")
        private fun setLikePopupMenu(id: String) {
            likeBtn.setOnClickListener {
                val popupMenu = PopupMenu(view.context, it)
                popupMenu.inflate(R.menu.like_curhat_menu_items)

                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.like_curhat_thumb_up -> {
                            CurhatRepository.addLikeReaction(id, CurhatReaction.THUMB_UP) {
                                CurhatViewUtil.changeReactionBtnColor(
                                    likeBtn, R.color.green, view
                                )
                            }
                            true
                        }
                        R.id.like_curhat_cool -> {
                            CurhatRepository.addLikeReaction(id, CurhatReaction.COOL) {
                                CurhatViewUtil.changeReactionBtnColor(
                                    likeBtn, R.color.green, view
                                )
                            }
                            true
                        }
                        R.id.like_curhat_love -> {
                            CurhatRepository.addLikeReaction(id, CurhatReaction.LOVE) {
                                CurhatViewUtil.changeReactionBtnColor(
                                    likeBtn, R.color.green, view
                                )
                            }
                            true
                        }

                        else -> false
                    }
                }

                val popupHelper = MenuPopupHelper(view.context, popupMenu.menu as MenuBuilder, it)
                popupHelper.setForceShowIcon(true)
                popupHelper.show()
            }
        }

        private fun setOnViewMoreListener(id: String) {
            viewMoreBtn.setOnClickListener {
                moveToCurhatDetail(id)
            }
        }

        private fun moveToCurhatDetail(id: String) {
            val intent = Intent(view.context, CurhatDetailActivity::class.java)
            val b = Bundle()
            b.putString("id", id);
            intent.putExtras(b)
            view.context.startActivity(intent)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {

                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.curhat_card_item, parent, false)

                return ViewHolder(view)
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

