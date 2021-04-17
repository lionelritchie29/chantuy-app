package edu.bluejack20_2.chantuy.views

import android.content.Intent
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
import edu.bluejack20_2.chantuy.utils.CurhatViewUtil
import edu.bluejack20_2.chantuy.views.curhat_detail.CurhatDetailActivity

class CurhatAdapter() : ListAdapter<Curhat, CurhatAdapter.ViewHolder>(CurhatDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.curhat_card_item, parent, false)

        return ViewHolder(view)
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

        fun bind(curhat: Curhat) {
            content.text = curhat.content
            postedDate.text = CurhatViewUtil.formatDate(curhat.createdAt)
            setOnViewMoreListener()
        }

        private fun setOnViewMoreListener() {
            viewMoreBtn.setOnClickListener {
                moveToCurhatDetail()
            }
        }

        private fun moveToCurhatDetail() {
            val intent = Intent(view.context, CurhatDetailActivity::class.java)
            view.context.startActivity(intent)
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

