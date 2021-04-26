package edu.bluejack20_2.chantuy.views.curhat_by_topic

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
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
import edu.bluejack20_2.chantuy.utils.InputUtil
import edu.bluejack20_2.chantuy.views.CurhatAdapter
import edu.bluejack20_2.chantuy.views.CurhatTopicChipAdapter

class CurhatByTopicFragment : Fragment() {

    private lateinit var binding: FragmentCurhatByTopicBinding
    private var isScrollingUp = false

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
            InputUtil.hideKeyboardFrom(binding.root.context, binding.root)
            isScrollingUp = true
            binding.filterTopicAutoComplete.setText("")
        }

        binding.filterTopicAutoComplete.setOnClickListener {
            isScrollingUp = false
        }

        viewModel.topics.observe(viewLifecycleOwner, Observer {
            binding.exampleTopicsLabel.text = it.map { topic -> topic.name }.toString()
        })

        viewModel.filteredCurhats.observe(viewLifecycleOwner, Observer {
            curhatAdapter.submitList(it)
        })

        return binding.root
    }

    private fun setFilteredCurhatRecyclerView(): CurhatAdapter {
        val adapter = CurhatAdapter()

        binding.filterTopicCurhatRecycler.adapter = adapter
        binding.filterTopicCurhatRecycler.layoutManager =
            object : LinearLayoutManager(activity, VERTICAL, false) {
                override fun canScrollVertically(): Boolean = true
            }

        binding.filterTopicCurhatRecycler.addOnScrollListener(object:
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (dy < 0) { //scroll down
                    if (!isScrollingUp) {
                        binding.filterTopicCard.startAnimation(AnimationUtils.loadAnimation(
                            binding.root.context, R.anim.trans_down
                        ))
                        isScrollingUp = true
                    }
                } else {
                    if (isScrollingUp) {
                        binding.filterTopicCard.startAnimation(AnimationUtils.loadAnimation(
                            binding.root.context, R.anim.trans_up
                        ))
                        isScrollingUp = false
                    }
                }
            }
        })

        return adapter
    }
}