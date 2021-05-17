package edu.bluejack20_2.chantuy.views.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.views.NotificationAdapter
import edu.bluejack20_2.chantuy.views.Text
import edu.bluejack20_2.chantuy.views.TextAdapter

class NotificationActivity : AppCompatActivity() {
    val viewModel=NotificationViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_notification)
        val viewModel= NotificationViewModel()
        addButtonListener(viewModel)
        val notificationAdapter=TextAdapter(
            viewModel.nList.value!!
        )


        val notificationRV: RecyclerView = findViewById(R.id.notification_comment_rv)
        notificationRV.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        notificationRV.adapter=notificationAdapter



        viewModel.nList.observe(this, Observer {

            notificationAdapter.setList(viewModel.nList.value!!)

        })


    }
    fun addButtonListener(viewModel: NotificationViewModel){
        val btn: Button = findViewById(R.id.mn_btn)
        btn.setOnClickListener {
            viewModel.getData()
        }
    }


}