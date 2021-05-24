package edu.bluejack20_2.chantuy.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.chantuy.databinding.CurhatCardItemBinding
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.repositories.CurhatRepository
import edu.bluejack20_2.chantuy.repositories.UserRepository
import edu.bluejack20_2.chantuy.utils.CurhatUtil
import edu.bluejack20_2.chantuy.utils.CurhatViewUtil

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
            setContent(curhat)
            binding.curhatCardLikeCount.text = curhat.likeCount.toString()
            binding.curhatCardDislikeCount.text = curhat.dislikeCount.toString()
            CurhatRepository.incrementViewCount(curhat.id)

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

            binding.curhatCardViewBtn.setOnClickListener {
                CurhatUtil.moveToCurhatDetail(curhat.id, binding.root.context)
            }

            binding.curhatCardInfoBtn.setOnClickListener {
                CurhatViewUtil.showCurhatInfoModal(curhat, binding.root.context)
            }

//            listenToChanges(curhat.id)
        }

        private fun listenToChanges(id: String) {
            FirebaseFirestore.getInstance().collection("curhats").document(id).addSnapshotListener { value, e ->
                val curhatListen = value?.toObject(Curhat::class.java)

                if (curhatListen != null) {
                    binding.curhatCardContent.text = CurhatViewUtil.trim(curhatListen.content)
                    binding.curhatCardLikeCount.text = curhatListen.likeCount.toString()
                    binding.curhatCardDislikeCount.text = curhatListen.dislikeCount.toString()
                    updateEditedState(curhatListen.createdAt!!, curhatListen.updatedAt!!)
                }
            }
        }

        private fun setContent(curhat: Curhat) {
            binding.curhatCardContent.text = CurhatViewUtil.trim(curhat.content)
            binding.curhatCardDate.text = CurhatViewUtil.formatDate(curhat.createdAt, binding.root.context)
            updateEditedState(curhat.createdAt!!, curhat.updatedAt!!)
        }

        private fun updateEditedState(createdAt: Timestamp, updatedAt: Timestamp) {
            if (updatedAt != createdAt) {
                binding.curhatCardEdited.visibility = View.VISIBLE
            } else {
                binding.curhatCardEdited.visibility = View.GONE
            }
        }

        private fun updateLikeDislikeCount(curhatId: String) {
            CurhatRepository.getLikeDislikeCount(curhatId) { likeCount: Long, dislikeCount: Long ->
                binding.curhatCardLikeCount.text = likeCount.toString()
                binding.curhatCardDislikeCount.text = dislikeCount.toString()
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

