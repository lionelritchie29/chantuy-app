package edu.bluejack20_2.chantuy.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.models.CurhatComment

class CurhatCommentAdapter : ListAdapter<CurhatComment, CurhatCommentAdapter.ViewHolder>(CurhatCommentDiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.curhat_comment_card_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = getItem(position)
        holder.bind(comment)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(comment: CurhatComment) {

        }
    }
}

object CurhatCommentDiffCallback : DiffUtil.ItemCallback<CurhatComment>() {
    override fun areItemsTheSame(oldItem: CurhatComment, newItem: CurhatComment): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CurhatComment, newItem: CurhatComment): Boolean {
        return oldItem.id == newItem.id
    }
}