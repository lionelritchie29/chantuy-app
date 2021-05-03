package edu.bluejack20_2.chantuy.views.search_curhat


import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.views.CurhatAdapter


class SearchCurhatFragment : Fragment() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_search_curhat, container, false)
        val viewModel=SearchCurhatViewModel()
        var searchButton: Button= rootView.findViewById(R.id.search_curhat_btn)
        var filterButton: ImageButton= rootView.findViewById(R.id.filter_button)
        val searchTextView: EditText= rootView.findViewById(R.id.search_auto_complete)
        val rv: RecyclerView = rootView.findViewById(R.id.search_curhat_recycler_view)
        rv.layoutManager=LinearLayoutManager(this.activity)
        searchButton.setOnClickListener{
            viewModel.searchString=searchTextView.text.toString()
            viewModel.search()
        }
        filterButton.setOnClickListener{
            showRadioButtonDialog(rootView)
        }
        val curhatAdapter=CurhatAdapter()
        rv.adapter=curhatAdapter
        viewModel.curhats.observe(viewLifecycleOwner, Observer {curhats->
            curhatAdapter.submitList(curhats.toList())
        })




        return rootView
    }
    private fun showRadioButtonDialog(rootView: View) {

        // custom dialog
        val dialog = Dialog(rootView.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.filter_popup_choice)
        val stringList: MutableList<String> = ArrayList() // here is list
        stringList.add("Sort search by most like")
        stringList.add("Sort search by most dislike")
        stringList.add("Filter curhats posted last 24 hour")
        stringList.add("Filter curhats posted last week")
        stringList.add("Filter curhats posted last month")
        stringList.add("Filter curhats posted last year")

        val rg = dialog.findViewById(R.id.radio_group) as RadioGroup
        for (i in stringList.indices) {
            val rb =
                RadioButton(rootView.context) // dynamically creating RadioButton and adding to RadioGroup.
            rb.text = stringList[i]
            rg.addView(rb)
        }
        Log.i("Testing","a: "+rg.checkedRadioButtonId)
    }
}

