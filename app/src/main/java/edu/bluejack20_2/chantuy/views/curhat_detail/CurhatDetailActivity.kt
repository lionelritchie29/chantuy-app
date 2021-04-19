package edu.bluejack20_2.chantuy.views.curhat_detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.models.CurhatComment
import edu.bluejack20_2.chantuy.repositories.CurhatCommentRepository
import edu.bluejack20_2.chantuy.views.CurhatCommentAdapter

class CurhatDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_curhat_detail)

        val viewModel = CurhatDetailViewModel()
        val adapter = CurhatCommentAdapter()

        val progressIndicator: LinearProgressIndicator = findViewById(R.id.detailCurhatLoadIndicator)
        val recyclerView: RecyclerView = findViewById(R.id.curhat_comment_recycler)
        recyclerView.adapter = adapter

        viewModel.getCurhatDetail(intent)

        val addBtn: Button = findViewById(R.id.add_comment_btn)
        val addCommentTv: TextView = findViewById(R.id.add_comment_edit_text)
        addBtn.setOnClickListener {
            viewModel.addComment(addCommentTv)
        }

        viewModel.comments.observe(this , Observer { comments ->
            adapter.addHeaderAndSubmitList(viewModel.curhat as Curhat, comments)
        })

        viewModel.isFetchingData.observe(this, Observer { isFetchingData ->
            if (!isFetchingData) {
                progressIndicator.visibility = View.GONE
            } else {
                progressIndicator.visibility = View.VISIBLE
            }
        })
    }
}