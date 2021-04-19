package edu.bluejack20_2.chantuy.views

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.chantuy.MainActivity
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.models.CurhatComment
import edu.bluejack20_2.chantuy.repositories.CurhatRepository
import edu.bluejack20_2.chantuy.utils.CurhatViewUtil
import io.grpc.okhttp.internal.framed.Header
import org.w3c.dom.Text
import kotlin.random.Random

class CurhatCommentAdapter : ListAdapter<DataItem, RecyclerView.ViewHolder>(CurhatCommentDiffCallback){

    private val ITEM_VIEW_TYPE_HEADER = 0
    private val ITEM_VIEW_TYPE_COMMENT = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_COMMENT -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is DataItem.DetailHeader -> ITEM_VIEW_TYPE_HEADER
            is DataItem.CurhatCommentItem -> ITEM_VIEW_TYPE_COMMENT
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val item = getItem(position) as DataItem.CurhatCommentItem
                holder.bind(item.comment)
            }
            is HeaderViewHolder -> {
                val item = getItem(position) as DataItem.DetailHeader
                holder.bind(item.curhat, item.commentCount)
            }
        }
    }

    fun addHeaderAndSubmitList(curhat: Curhat, list: List<CurhatComment>) {
        val items = when (list) {
            null -> listOf(DataItem.DetailHeader(curhat, 0))
            else -> listOf(DataItem.DetailHeader(curhat, list.size)) + list.map { DataItem.CurhatCommentItem(it) }
        }
        submitList(items)
    }



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.curhat_comment_user_name)
        val content: TextView = view.findViewById(R.id.curhat_comment_content)
        val createdAt: TextView = view.findViewById(R.id.curhat_comment_date)

        fun bind(comment: CurhatComment) {
            name.text = "Anonymous"
            content.text = comment.content
            createdAt.text = CurhatViewUtil.formatDate(comment.createdAt)
        }

        companion object {
            fun from(parent: ViewGroup) : ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.curhat_comment_card_item, parent, false)
                return ViewHolder(view)
            }
        }
    }

    class HeaderViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val name : TextView = view.findViewById(R.id.curhat_detail_user_name)
        val content: TextView = view.findViewById(R.id.curhat_detail_content)
        val createdAt: TextView = view.findViewById(R.id.curhat_detail_date)
        val commentCountText: TextView = view.findViewById(R.id.curhat_detail_comment_count)
        val actionBtn: ImageButton = view.findViewById(R.id.curhat_detail_action_btn)

        fun bind(curhat: Curhat, commentCount: Int) {
            name.text = "Anonymous"
            content.text = curhat.content
            createdAt.text = CurhatViewUtil.formatDate(curhat.createdAt)
            commentCountText.text = commentCount.toString() + " comment(s)"

            setActionMenu(curhat)
        }

        private fun setActionMenu(curhat: Curhat) {
            actionBtn.setOnClickListener {
                val popupMenu = PopupMenu(view.context, it)
                popupMenu.menuInflater.inflate(R.menu.curhat_detail_action_items, popupMenu.menu)
                popupMenu.show()

                setMenuOnClickListener(popupMenu, curhat)
            }
        }

        private fun setMenuOnClickListener(popupMenu: PopupMenu, curhat: Curhat) {
            popupMenu.setOnMenuItemClickListener {menuItem ->
                when (menuItem.itemId) {
                    R.id.update_curhat_menu_item -> onUpdate()
                    R.id.delete_curhat_menu_item -> onDelete(curhat)
                    else -> false
                }
            }
        }

        private fun onUpdate(): Boolean {
            return true
        }

        private fun onDelete(curhat: Curhat): Boolean {
            val builder = AlertDialog.Builder(view.context)
            builder.setMessage("Are you sure ?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    deleteCurhatAndComments(curhat)
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
            return true
        }

        private fun deleteCurhatAndComments(curhat: Curhat) {
            CurhatRepository.deleteById(curhat.id) {
                val currentActivity = view.context as Activity
                currentActivity.finish()
            }
        }

        companion object {
            fun from(parent: ViewGroup) : HeaderViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.curhat_detail_header, parent, false)
                return HeaderViewHolder(view)
            }
        }
    }
}

object CurhatCommentDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }
}

sealed class DataItem {
    data class CurhatCommentItem(val comment: CurhatComment): DataItem() {
        override val id: String
            get() = comment.createdAt.toString()
    }

    data class DetailHeader(val curhat: Curhat, val commentCount: Int): DataItem() {
        override val id: String
            get() = curhat.id
    }

    abstract val id: String
}