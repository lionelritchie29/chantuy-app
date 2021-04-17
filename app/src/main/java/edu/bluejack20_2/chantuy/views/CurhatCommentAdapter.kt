package edu.bluejack20_2.chantuy.views

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.models.CurhatComment
import io.grpc.okhttp.internal.framed.Header
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
                holder.bind()
            }
        }
    }

    fun addHeaderAndSubmitList(list: List<CurhatComment>) {
        val items = when (list) {
            null -> listOf(DataItem.DetailHeader)
            else -> listOf(DataItem.DetailHeader) + list.map { DataItem.CurhatCommentItem(it) }
        }
        submitList(items)
    }



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(comment: CurhatComment) {

        }

        companion object {
            fun from(parent: ViewGroup) : ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.curhat_comment_card_item, parent, false)
                return ViewHolder(view)
            }
        }
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {

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
            get() = comment.id
    }

    object DetailHeader: DataItem() {
        override val id: String
            get() = Random.nextInt().toString()
    }

    abstract val id: String
}