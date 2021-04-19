package edu.bluejack20_2.chantuy.views.hottest_curhat

import HottestCurhatViewModel
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.views.CurhatAdapter

class HottestCurhatFragment : Fragment() {
    private lateinit var viewModel: HottestCurhatViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_hottest_curhat, container, false)
        viewModel = HottestCurhatViewModel()
        val curhatAdapter = CurhatAdapter()
        val recyclerView: RecyclerView = rootView.findViewById(R.id.curhat_recycler_view)
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val progressIndicator: LinearProgressIndicator = rootView.findViewById(R.id.newestCurhatLoadIndicator)

        recyclerView.adapter = curhatAdapter
        recyclerView.layoutManager = manager

        viewModel.curhats.observe(viewLifecycleOwner, Observer {curhats ->
            curhatAdapter.submitList(curhats)

            if (curhats.size > 0) {
                viewModel.lastCurhat = curhats.get(curhats.size - 1)
            }
        })

        viewModel.isFetchingData.observe(viewLifecycleOwner, Observer { isFetchingData ->
            if (!isFetchingData) {
                progressIndicator.visibility = View.GONE
            } else {
                progressIndicator.visibility = View.VISIBLE
            }
        })

        viewModel.handleOnScrollListener(recyclerView, manager)

        return rootView
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadData()
    }
}