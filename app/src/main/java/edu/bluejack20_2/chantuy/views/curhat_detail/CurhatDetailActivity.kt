package edu.bluejack20_2.chantuy.views.curhat_detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.models.CurhatComment
import edu.bluejack20_2.chantuy.views.CurhatCommentAdapter

class CurhatDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_curhat_detail)

        val viewModel = CurhatDetailViewModel()
        val adapter = CurhatCommentAdapter()
        val recyclerView: RecyclerView = findViewById(R.id.curhat_comment_recycler)
        recyclerView.adapter = adapter

        viewModel.comments.observe(this , Observer {
            adapter.addHeaderAndSubmitList(it)
            Log.i("CurhatDetailActivity", it.toString())
        })
    }
}