package edu.bluejack20_2.chantuy.views.newest_curhat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.views.CurhatAdapter

class NewestCurhatFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_newest_curhat, container, false)
        val viewModel = NewestCurhatViewModel()
        val curhatAdapter = CurhatAdapter()
        val recyclerView: RecyclerView = rootView.findViewById(R.id.newest_curhat_recycler)
        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        recyclerView.adapter = curhatAdapter
        recyclerView.layoutManager = manager

        viewModel.handleOnScrollListener(recyclerView, manager)

        viewModel.curhats.observe(viewLifecycleOwner, Observer {curhats ->
            curhatAdapter.submitList(curhats)

            if (curhats.size > 0) {
                viewModel.lastCurhat = curhats.get(curhats.size - 1)
            }
        })

        return rootView
    }
}