package edu.bluejack20_2.chantuy.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.repositories.CurhatCommentRepository
import edu.bluejack20_2.chantuy.repositories.CurhatRepository
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
        private val content : TextView = view.findViewById(R.id.curhat_card_content)
        private val username: TextView = view.findViewById(R.id.curhat_card_username)
        private val postedDate: TextView = view.findViewById(R.id.curhat_card_date)
        private val viewMoreBtn: Button = view.findViewById(R.id.curhat_card_view_btn)
        private val commentCount: TextView = view.findViewById(R.id.curhat_card_comment_count)

        fun bind(curhat: Curhat) {
            username.text = if (curhat.isAnonymous) "Anonymous" else "Name"
            content.text = curhat.content
            postedDate.text = CurhatViewUtil.formatDate(curhat.createdAt)
            CurhatRepository.incrementViewCount(curhat.id)
            setOnViewMoreListener(curhat.id)

            CurhatCommentRepository.getCommentCount(curhat.id) { count ->
                commentCount.text = count.toString()
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
            fun from(parent: ViewGroup) : ViewHolder {
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

