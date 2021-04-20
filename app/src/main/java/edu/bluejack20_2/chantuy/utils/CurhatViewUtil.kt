package edu.bluejack20_2.chantuy.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import com.google.firebase.Timestamp
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.databinding.CurhatCardItemBinding
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.models.CurhatReaction
import edu.bluejack20_2.chantuy.repositories.CurhatRepository
import edu.bluejack20_2.chantuy.repositories.UserRepository
import java.text.SimpleDateFormat
import java.util.*

class CurhatViewUtil {
    companion object {
        fun formatDate(timestamp: Timestamp?) : String {
            val sdf = SimpleDateFormat("dd-MM-yyyy 'at' HH:mm:ss")
            var formatted: String = ""

            if (timestamp != null) {
                formatted = sdf.format(timestamp?.toDate())
            }
            return formatted
        }

        fun changeReactionBtnColor(btn: ImageButton, colorId: Int, view: View) {
            btn.setColorFilter(
                ContextCompat.getColor(view.context, colorId),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
        }

        @SuppressLint("RestrictedApi")
        fun setDislikePopupMenu(dislikeBtn: ImageButton, id: String, view: View) {
            dislikeBtn.setOnClickListener {
                val popupMenu = PopupMenu(view.context, it)
                popupMenu.inflate(R.menu.dislike_curhat_menu_items)

                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.dislike_curhat_thumb_down -> {
                            CurhatRepository.addDislikeReaction(id, CurhatReaction.THUMB_DOWN) {
                                changeReactionBtnColor(
                                    dislikeBtn, R.color.dark_red, view
                                )
                            }
                            true
                        }
                        R.id.dislike_curhat_angry -> {
                            CurhatRepository.addDislikeReaction(id, CurhatReaction.ANGRY) {
                                changeReactionBtnColor(
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
        fun setLikePopupMenu(likeBtn: ImageButton, id: String, view: View) {
            likeBtn.setOnClickListener {
                val popupMenu = PopupMenu(view.context, it)
                popupMenu.inflate(R.menu.like_curhat_menu_items)

                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.like_curhat_thumb_up -> {
                            CurhatRepository.addLikeReaction(id, CurhatReaction.THUMB_UP) {
                                changeReactionBtnColor(
                                    likeBtn, R.color.green, view
                                )
                            }
                            true
                        }
                        R.id.like_curhat_cool -> {
                            CurhatRepository.addLikeReaction(id, CurhatReaction.COOL) {
                                changeReactionBtnColor(
                                    likeBtn, R.color.green, view
                                )
                            }
                            true
                        }
                        R.id.like_curhat_love -> {
                            CurhatRepository.addLikeReaction(id, CurhatReaction.LOVE) {
                                changeReactionBtnColor(
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

        fun setReactionBtnColor(likeBtn: ImageButton, dislikeBtn: ImageButton, curhat: Curhat, view: View) {
            if (curhat == null) return
            val userId = UserRepository.getCurrentUserId()

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
    }

}