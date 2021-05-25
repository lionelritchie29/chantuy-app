package edu.bluejack20_2.chantuy.views.newest_curhat

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.progressindicator.LinearProgressIndicator
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.databinding.FragmentNewestCurhatBinding
import edu.bluejack20_2.chantuy.models.GLOBALS
import edu.bluejack20_2.chantuy.utils.CurhatUtil
import edu.bluejack20_2.chantuy.views.CurhatAdapter

class NewestCurhatFragment : Fragment() {
    private lateinit var manager: LinearLayoutManager
    private lateinit var binding: FragmentNewestCurhatBinding
    private lateinit var viewModel: NewestCurhatViewModel
    private lateinit var curhatAdapter: CurhatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_newest_curhat, container, false)
        viewModel = NewestCurhatViewModel()
        curhatAdapter = CurhatAdapter()
        manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        binding.newestCurhatRecycler.adapter = curhatAdapter
        binding.newestCurhatRecycler.layoutManager = manager

        viewModel.handleOnScrollListener(binding.newestCurhatRecycler, manager)

        viewModel.curhats.observe(viewLifecycleOwner, Observer {curhats ->

            curhatAdapter.submitList(curhats)
            if (curhats.size > 0) {
                viewModel.lastCurhat = curhats.get(curhats.size - 1)
            }
        })

        viewModel.isSizeZero.observe(viewLifecycleOwner, Observer { isSizeZero ->
            if (isSizeZero) {
                binding.newestNoCurhatImg.visibility = View.VISIBLE
            } else {
                binding.newestNoCurhatImg.visibility = View.GONE
            }
        })

        viewModel.isFetchingData.observe(viewLifecycleOwner, Observer { isFetchingData ->
            if (!isFetchingData) {
                binding.newestGettingCurhatImg.visibility = View.GONE
                binding.detailCurhatLoadIndicator.visibility = View.GONE
            } else {
                binding.newestGettingCurhatImg.visibility = View.VISIBLE
                binding.detailCurhatLoadIndicator.visibility = View.VISIBLE
            }
        })

        binding.swipeContainer.setOnRefreshListener(object: SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                viewModel.loadData { binding.swipeContainer.isRefreshing = false }
            }

        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        CurhatUtil.refreshCurhatsIfUpdated(binding.root.context) {
            viewModel.loadData {  }
        }
    }
}