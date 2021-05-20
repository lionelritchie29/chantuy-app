package edu.bluejack20_2.chantuy.views

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.LayoutRes
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.models.CurhatTopic
import edu.bluejack20_2.chantuy.repositories.CurhatTopicRepository

class TopicAutoCompleteAdapter(
    context: Context,
    @LayoutRes private val layoutResource: Int,
    private val allTopics: List<String>
) : ArrayAdapter<String>(context, layoutResource, allTopics), Filterable {

    var topics = allTopics
    var allTopicsContainer = allTopics
    private val MAX_RESULT = 5

    override fun getCount(): Int {
        return topics.size
    }

    override fun getItem(position: Int): String {
        return topics[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(layoutResource, parent, false) as TextView
        view.text = topics[position]
        return view
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint.toString().toLowerCase()

                val filterResults = FilterResults()

                if (query.isNotEmpty()) {
                    filterResults.values = allTopicsContainer.filter{t -> t.contains(query)}.take(MAX_RESULT)
                } else {
                    filterResults.values = allTopicsContainer
                }

                Log.i("TopicAdapter", "Init Topic: " + allTopicsContainer.toString())
                Log.i("TopicAdapter", filterResults.values.toString())
                Log.i("TopicAdapter", filterResults.values::class.java.toString())

                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                topics = results?.values as List<String>
                notifyDataSetChanged()
            }
        }
    }
}