package edu.bluejack20_2.chantuy.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.chantuy.databinding.CurhatCardItemBinding
import edu.bluejack20_2.chantuy.databinding.NotificationItemBinding
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.models.CurhatComment
import edu.bluejack20_2.chantuy.models.Notification
import edu.bluejack20_2.chantuy.repositories.CurhatCommentRepository
import edu.bluejack20_2.chantuy.repositories.UserRepository
import edu.bluejack20_2.chantuy.utils.CurhatUtil
import edu.bluejack20_2.chantuy.utils.CurhatViewUtil

class NotificationAdapter: ListAdapter<Notification, NotificationAdapter.ViewHolder> (NotificationDiffCallback)  {

    class ViewHolder(var binding: NotificationItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(notif: Notification) {
            notif.commentId?.let {
                binding.notificationDate.text = CurhatViewUtil.formatDate(notif.createdAt)
                CurhatCommentRepository.getComment(it).addOnSuccessListener {
                    val comment = it.toObject(CurhatComment::class.java)
                    binding.notificationComment.text = "'${comment?.content}'"

                    if (comment != null) {
                        UserRepository.getUserById(comment.user) {user ->
                            binding.notificationUsername.text = user?.name
                            CurhatViewUtil.setCurhatUserImage(false, user!!, binding.notificationUserImage, binding.root)
                        }

                        binding.notifViewBtn.setOnClickListener {
                            CurhatUtil.moveToCurhatDetail(comment.curhatId, it.context)
                        }
                    }
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NotificationItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

object NotificationDiffCallback : DiffUtil.ItemCallback<Notification>() {
    override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem.id == newItem.id
    }
}

