package edu.bluejack20_2.chantuy.views.hottest_curhat

import HottestCurhatViewModel
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.progressindicator.LinearProgressIndicator
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.views.CurhatAdapter
import edu.bluejack20_2.chantuy.databinding.FragmentHottestCurhatBinding

class HottestCurhatFragment : Fragment() {

    private lateinit var binding: FragmentHottestCurhatBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hottest_curhat, container, false)

        val viewModel = HottestCurhatViewModel()
        val curhatAdapter = CurhatAdapter()
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        binding.curhatRecyclerView.adapter = curhatAdapter
        binding.curhatRecyclerView.layoutManager = manager

        viewModel.curhats.observe(viewLifecycleOwner, Observer {curhats ->
            curhatAdapter.submitList(curhats)

            if (curhats.size > 0) {
                viewModel.lastCurhat = curhats.get(curhats.size - 1)
            }
        })

        viewModel.isSizeZero.observe(viewLifecycleOwner, Observer {isZero ->
            Log.i("HottestCurhatFragment", isZero.toString())
            if (isZero) {
                Log.i("HottestCurhatFragment", "is true: " + isZero.toString())
                binding.hottestNoCurhatImg.visibility = View.VISIBLE
            } else {
                binding.hottestNoCurhatImg.visibility = View.GONE
            }
        })

        viewModel.isFetchingData.observe(viewLifecycleOwner, Observer { isFetchingData ->
            if (!isFetchingData) {
                binding.hottestGettingCurhatImg.visibility = View.GONE
                binding.detailCurhatLoadIndicator.visibility = View.GONE
            } else {
                binding.hottestGettingCurhatImg.visibility = View.VISIBLE
                binding.detailCurhatLoadIndicator.visibility = View.VISIBLE
            }
        })

        binding.swipeContainer.setOnRefreshListener(object: SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                viewModel.loadData { binding.swipeContainer.isRefreshing = false }
            }
        })

        viewModel.handleOnScrollListener(binding.curhatRecyclerView, manager)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
//        viewModel.loadData()
    }
}