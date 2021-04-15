package edu.bluejack20_2.chantuy.views.hottest_curhat

import HottestCurhatViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.views.CurhatAdapter

class HottestCurhatFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_hottest_curhat, container, false)
        val viewModel = HottestCurhatViewModel()
        val curhatAdapter = CurhatAdapter()
        val recyclerView: RecyclerView = rootView.findViewById(R.id.curhat_recycler_view)
        recyclerView.adapter = curhatAdapter

        viewModel.curhats.observe(viewLifecycleOwner, Observer {curhats ->
            curhatAdapter.submitList(curhats)
        })

        return rootView
    }
}