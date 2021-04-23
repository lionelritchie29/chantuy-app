package edu.bluejack20_2.chantuy.views.feedback

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.views.FeedbackAdapter

class FeedbackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        val viewModel = FeedbackViewModel()
        val adapter = FeedbackAdapter()
        val recycler = findViewById<RecyclerView>(R.id.feedback_recycler)
        recycler.adapter = adapter

        viewModel.feedbacks.observe(this, Observer {
            adapter.submitList(it)
        })
    }
}