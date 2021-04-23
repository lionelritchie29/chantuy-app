package edu.bluejack20_2.chantuy.views.curhat_by_topic

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.databinding.FragmentCurhatByTopicBinding
import edu.bluejack20_2.chantuy.views.CurhatAdapter
import edu.bluejack20_2.chantuy.views.CurhatTopicChipAdapter

class CurhatByTopicFragment : Fragment() {

    private lateinit var binding: FragmentCurhatByTopicBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_curhat_by_topic, container, false)
        val viewModel = CurhatByTopicViewModel(requireActivity().application)

//        val topicAdapter = setTopicChipRecyclerView(view)
        val curhatAdapter = setFilteredCurhatRecyclerView()
        viewModel.setTopicAutocomplete(binding.filterTopicAutoComplete)

        binding.filterTopicBtn.setOnClickListener {
            viewModel.OnFilter(binding.filterTopicAutoComplete.text.toString())
        }

        viewModel.topics.observe(viewLifecycleOwner, Observer {
            binding.exampleTopicsLabel.text = it.map { topic -> topic.name }.toString()
        })

        viewModel.filteredCurhats.observe(viewLifecycleOwner, Observer {
            curhatAdapter.submitList(it)
        })

        return binding.root
    }

//    private fun setTopicChipRecyclerView(view: View) : CurhatTopicChipAdapter {
//        val adapter = CurhatTopicChipAdapter()
//        val chipRecyclerView: RecyclerView = view.findViewById(R.id.filter_topic_chip_recycler)
//        val manager = object: LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false) {
//            override fun canScrollVertically(): Boolean = false
//        }
//
//        chipRecyclerView.adapter = adapter
//        chipRecyclerView.layoutManager = manager
//
//        return adapter
//    }

    private fun setFilteredCurhatRecyclerView(): CurhatAdapter {
        val adapter = CurhatAdapter()

        binding.filterTopicCurhatRecycler.adapter = adapter
        binding.filterTopicCurhatRecycler.layoutManager =
            object : LinearLayoutManager(activity, VERTICAL, false) {
                override fun canScrollVertically(): Boolean = true
            }

        return adapter
    }
}