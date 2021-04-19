package edu.bluejack20_2.chantuy.views.curhat_by_topic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.views.CurhatAdapter
import edu.bluejack20_2.chantuy.views.CurhatTopicChipAdapter

class CurhatByTopicFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_curhat_by_topic, container, false)
        val viewModel = CurhatByTopicViewModel(activity!!.application)

        val topicAdapter = setTopicChipRecyclerView(view)
        val curhatAdapter = setFilteredCurhatRecyclerView(view)

        val autoCompleteTv: AutoCompleteTextView = view.findViewById(R.id.filter_topic_auto_complete)
        viewModel.setTopicAutocomplete(autoCompleteTv)

        val filterBtn: Button = view.findViewById(R.id.filter_topic_btn)
        filterBtn.setOnClickListener {
            viewModel.OnFilter(autoCompleteTv.text.toString())
        }

        viewModel.topics.observe(this, Observer {
            topicAdapter.submitList(it)
        })

        viewModel.filteredCurhats.observe(this, Observer {
            curhatAdapter.submitList(it)
        })

        return view
    }

    private fun setTopicChipRecyclerView(view: View) : CurhatTopicChipAdapter {
        val adapter = CurhatTopicChipAdapter()
        val chipRecyclerView: RecyclerView = view.findViewById(R.id.filter_topic_chip_recycler)
        val manager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        chipRecyclerView.adapter = adapter
        chipRecyclerView.layoutManager = manager

        return adapter
    }

    private fun setFilteredCurhatRecyclerView(view: View) : CurhatAdapter {
        val adapter = CurhatAdapter()
        val curhatRecyclerView: RecyclerView = view.findViewById(R.id.filter_topic_curhat_recycler)

        curhatRecyclerView.adapter = adapter
        curhatRecyclerView.layoutManager = object : LinearLayoutManager(activity, VERTICAL, false) {
            override fun canScrollVertically(): Boolean = false
        }

        return adapter
    }
}