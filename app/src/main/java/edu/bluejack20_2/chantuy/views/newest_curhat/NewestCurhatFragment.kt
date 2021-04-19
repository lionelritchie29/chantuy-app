package edu.bluejack20_2.chantuy.views.newest_curhat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.views.CurhatAdapter

class NewestCurhatFragment : Fragment() {
    private lateinit var viewModel: NewestCurhatViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_newest_curhat, container, false)
        viewModel = NewestCurhatViewModel()
        val curhatAdapter = CurhatAdapter()
        recyclerView = rootView.findViewById(R.id.newest_curhat_recycler)
        manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val progressIndicator: LinearProgressIndicator = rootView.findViewById(R.id.detailCurhatLoadIndicator)

        recyclerView.adapter = curhatAdapter
        recyclerView.layoutManager = manager

        viewModel.handleOnScrollListener(recyclerView, manager)

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

        return rootView
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadData()
    }


}