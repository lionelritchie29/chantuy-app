package edu.bluejack20_2.chantuy.views

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.databinding.*
import edu.bluejack20_2.chantuy.views.update_curhat.UpdateCurhatActivity
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.models.CurhatComment
import edu.bluejack20_2.chantuy.repositories.CurhatCommentRepository
import edu.bluejack20_2.chantuy.repositories.CurhatRepository
import edu.bluejack20_2.chantuy.repositories.UserRepository
import edu.bluejack20_2.chantuy.utils.CurhatViewUtil
import kotlin.random.Random

class CurhatCommentAdapter (private val callback: () -> Unit ) : ListAdapter<DataItem, RecyclerView.ViewHolder>(CurhatCommentDiffCallback){

    private val ITEM_VIEW_TYPE_HEADER = 0
    private val ITEM_VIEW_TYPE_COMMENT = 1
    private val ITEM_VIEW_TYPE_SHOW_MORE = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_COMMENT -> ViewHolder.from(parent)
            ITEM_VIEW_TYPE_SHOW_MORE -> ShowMoreViewHolder.from(parent, callback)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is DataItem.DetailHeader -> ITEM_VIEW_TYPE_HEADER
            is DataItem.CurhatCommentItem -> ITEM_VIEW_TYPE_COMMENT
            is DataItem.ShowMoreItem -> ITEM_VIEW_TYPE_SHOW_MORE
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
                holder.bind(item.curhat)
            }
            is ShowMoreViewHolder -> {
                val item = getItem(position) as DataItem.ShowMoreItem
                holder.bind(item)
            }
        }
    }

    fun addHeaderAndSubmitList(curhat: Curhat, list: List<CurhatComment>) {
        CurhatCommentRepository.getCommentCount(curhat.id) {commentCount ->
            val items = when (list) {
                null -> listOf(DataItem.DetailHeader(curhat))
                else -> {
                    listOf(DataItem.DetailHeader(curhat)) + list.map { DataItem.CurhatCommentItem(it) } +
                            if (commentCount != list.size) listOf(DataItem.ShowMoreItem())
                            else listOf()
                }
            }
            submitList(items)
        }
    }

    class ViewHolder(val binding: CurhatCommentCardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var isUpdating = false

        fun bind(comment: CurhatComment) {
            binding.curhatCommentContent.text = comment.content
            binding.curhatCommentDateFirst.text = CurhatViewUtil.formatDate(comment.createdAt, binding.root.context)
            binding.curhatCommentEditText.setText(comment.content)

            if (comment.user.length > 0) {
                UserRepository.getUserById(comment.user) {user ->
                    binding.curhatCommentUserName.text = "${user?.name} (${user?.age}, ${user?.gender})"
                    CurhatViewUtil.setCurhatUserImage(false, user!!, binding.curhatCommentUserImage, binding.root)
                }
            }

            if (comment.createdAt != comment.updatedAt) {
                binding.curhatCommentEdited.visibility = View.VISIBLE
            } else {
                binding.curhatCommentEdited.visibility = View.GONE
            }

            setActionBtnVisibility(comment.user)
            setActionBtnListener(comment)
            setUpdateEventListener(comment)
            setCancelEventListener(comment)
        }

        private fun setUpdateEventListener(comment: CurhatComment) {
            binding.curhatCommentUpdateBtn.setOnClickListener {
                if (binding.curhatCommentEditText.text.isEmpty()) {
                    binding.curhatCommentEditText.error = binding.root.context.getString(R.string.content_empty_error)
                } else {
                    CurhatCommentRepository.updateComment(
                        comment.commentId, binding.curhatCommentEditText.text.toString()
                    ) {
                        binding.curhatCommentContent.text = it
                        binding.curhatCommentEdited.visibility = View.VISIBLE
                        isUpdating = !isUpdating
                        toggleUpdateForm(comment, isUpdating)
                        Toast.makeText(binding.root.context, binding.root.context.getString(R.string.toast_update_succesfully), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        private fun setCancelEventListener(comment: CurhatComment) {
            binding.curhatCommentCancelBtn.setOnClickListener {
                isUpdating = !isUpdating
                toggleUpdateForm(comment, isUpdating)
            }
        }

        private fun setActionBtnVisibility(userId: String ) {
            if (UserRepository.getCurrentUserId() == userId) {
                binding.curhatCommentActionBtn.visibility = View.VISIBLE
            } else {
                binding.curhatCommentActionBtn.visibility = View.INVISIBLE
            }
        }

        private fun setActionBtnListener(comment: CurhatComment) {
            binding.curhatCommentActionBtn.setOnClickListener {
                val popupMenu = PopupMenu(binding.root.context, it)
                popupMenu.menuInflater.inflate(R.menu.curhat_detail_action_items, popupMenu.menu)

                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.update_curhat_menu_item -> {
                            isUpdating = !isUpdating
                            toggleUpdateForm(comment, isUpdating)
                        }
                        R.id.delete_curhat_menu_item -> deleteComment(comment)
                        else -> false
                    }
                }

                popupMenu.show()
            }
        }

        fun toggleUpdateForm(comment: CurhatComment, isUpdating: Boolean): Boolean {

            if (isUpdating) {
                CurhatCommentRepository.getComment(comment.commentId).addOnSuccessListener {
                    binding.curhatCommentEditText.setText(it.getString("content"))
                }
                binding.curhatCommentContent.visibility = View.GONE
                binding.curhatCommentDateFirst.visibility = View.GONE
                binding.curhatCommentEditText.visibility = View.VISIBLE
                binding.curhatCommentUpdateBtn.visibility = View.VISIBLE
                binding.curhatCommentCancelBtn.visibility = View.VISIBLE
            } else {
                binding.curhatCommentContent.visibility = View.VISIBLE
                binding.curhatCommentDateFirst.visibility = View.VISIBLE
                binding.curhatCommentEditText.visibility = View.GONE
                binding.curhatCommentUpdateBtn.visibility = View.GONE
                binding.curhatCommentCancelBtn.visibility = View.GONE
            }
            return true
        }

        private fun deleteComment(comment: CurhatComment): Boolean {
            val builder = AlertDialog.Builder(binding.root.context)
            builder.setMessage("Are you sure ?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    CurhatCommentRepository.deleteById(comment.curhatId, comment.commentId) {
                        Toast.makeText(binding.root.context, binding.root.context.getString(R.string.toast_cds) , Toast.LENGTH_SHORT).show()
                        reloadActivity()
                    }
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
            return true
        }

        private fun reloadActivity() {
            val activity = binding.root.context as Activity
            activity.finish()
            activity.overridePendingTransition(0 ,0)
            activity.startActivity(activity.intent)
            activity.overridePendingTransition(0 ,0)
        }

        companion object {
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CurhatCommentCardItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class HeaderViewHolder(val binding: CurhatDetailHeaderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(curhat: Curhat) {
            binding.curhatDetailContent.text = curhat.content
            binding.curhatDetailDate.text = CurhatViewUtil.formatDate(curhat.createdAt,binding.root.context)
            binding.curhatDetailCommentCount.text = curhat.commentCount.toString() + " " + binding.root.context.getString(R.string.comment)
            binding.curhatDetailLikeCount.text = curhat.likeCount.toString()
            binding.curhatDetailDislikeCount.text = curhat.dislikeCount.toString()

            if (curhat.updatedAt?.nanoseconds != curhat.createdAt?.nanoseconds) {
                binding.curhatDetailEdited.visibility = View.VISIBLE
            } else {
                binding.curhatDetailEdited.visibility = View.GONE
            }

            setActionBtnVisibility(curhat.user)

            if (curhat.user.length > 0) {
                UserRepository.getUserById(curhat.user) {user ->
                    binding.curhatDetailUserName.text = if (curhat.isAnonymous) "Anonymous" else user?.name
                    CurhatViewUtil.setCurhatUserImage(curhat.isAnonymous, user!!, binding.curhatDetailUserImage, binding.root)
                }
            }

            setActionMenu(curhat)
            CurhatViewUtil.setReactionBtnColor(
                binding.curhatDetailThumbUpBtn,
                binding.curhatDetailThumbDownBtn,
                curhat, binding.root
            )
            CurhatViewUtil.setLikePopupMenu(binding.curhatDetailThumbUpBtn, binding.curhatDetailThumbDownBtn, curhat, binding.root) {
                updateLikeDislikeCount(curhat.id)
            }
            CurhatViewUtil.setDislikePopupMenu(binding.curhatDetailThumbUpBtn, binding.curhatDetailThumbDownBtn, curhat, binding.root) {
                updateLikeDislikeCount(curhat.id)
            }

            binding.curhatDetailHeaderInfoBtn.setOnClickListener {
                CurhatViewUtil.showCurhatInfoModal(curhat, binding.root.context)
            }
        }

        private fun updateLikeDislikeCount(curhatId: String) {
            CurhatRepository.getLikeDislikeCount(curhatId) { likeCount: Long, dislikeCount: Long ->
                binding.curhatDetailLikeCount.text = likeCount.toString()
                binding.curhatDetailDislikeCount.text = dislikeCount.toString()
            }
        }

        private fun setActionBtnVisibility(userId: String) {
            if (UserRepository.getCurrentUserId() == userId) {
                binding.curhatDetailActionBtn.visibility = View.VISIBLE
            } else {
                binding.curhatDetailActionBtn.visibility = View.INVISIBLE
            }
        }

        private fun setActionMenu(curhat: Curhat) {
            binding.curhatDetailActionBtn.setOnClickListener {
                val popupMenu = PopupMenu(binding.root.context, it)
                popupMenu.menuInflater.inflate(R.menu.curhat_detail_action_items, popupMenu.menu)
                popupMenu.show()

                setMenuOnClickListener(popupMenu, curhat)
            }
        }

        private fun setMenuOnClickListener(popupMenu: PopupMenu, curhat: Curhat) {
            popupMenu.setOnMenuItemClickListener {menuItem ->
                when (menuItem.itemId) {
                    R.id.update_curhat_menu_item -> moveToUpdateActivity(curhat)
                    R.id.delete_curhat_menu_item -> onDelete(curhat)
                    else -> false
                }
            }
        }

        private fun moveToUpdateActivity(curhat: Curhat): Boolean {
            val intent = Intent(binding.root.context, UpdateCurhatActivity::class.java)
            val b = Bundle()
            b.putString("curhatId", curhat.id)
            intent.putExtras(b)

            val activity = binding.root.context as Activity
            activity.startActivity(intent)
            return true
        }

        private fun onDelete(curhat: Curhat): Boolean {
            val builder = AlertDialog.Builder(binding.root.context)
            builder.setMessage(binding.root.context.getString(R.string.ausure))
                .setCancelable(false)
                .setPositiveButton(binding.root.context.getString(R.string.yes)) { dialog, id ->
                    deleteCurhatAndComments(curhat)
                }
                .setNegativeButton(binding.root.context.getString(R.string.no)) { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
            return true
        }

        private fun deleteCurhatAndComments(curhat: Curhat) {
            CurhatRepository.deleteById(curhat.id) {
                val currentActivity = binding.root.context as Activity
                currentActivity.finish()
                Toast.makeText(binding.root.context, binding.root.context.getString(R.string.toast_cds), Toast.LENGTH_SHORT).show()
            }
        }

        companion object {
            fun from(parent: ViewGroup) : HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CurhatDetailHeaderBinding.inflate(layoutInflater, parent, false)
                return HeaderViewHolder(binding)
            }
        }
    }

    class ShowMoreViewHolder(val binding: ShowMoreCommentBinding, val callback: () -> Unit): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataItem.ShowMoreItem) {
            binding.showMoreCommentBtn.setOnClickListener {
                callback()
            }
        }

        companion object {
            fun from(parent: ViewGroup, callback: () -> Unit): ShowMoreViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ShowMoreCommentBinding.inflate(layoutInflater, parent, false)
                return ShowMoreViewHolder(binding, callback)
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
            get() = comment.commentId
    }

    data class DetailHeader(val curhat: Curhat): DataItem() {
        override val id: String
            get() = curhat.id
    }

    class ShowMoreItem(): DataItem() {
        override val id: String
            get() = Random.nextInt().toString()
    }

    abstract val id: String
}