package edu.bluejack20_2.chantuy.views.user_profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.Text
import com.example.todolist.TextAdapter
import com.firebase.ui.auth.AuthUI
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.views.CurhatAdapter
import edu.bluejack20_2.chantuy.views.login.LoginActivity


class UserProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_user_profile, container, false)

        val viewModel = UserProfileViewModel()

//        val recyclerView: RecyclerView = rootView.findViewById(R.id.curhat_recycler_view)kasi


        val curhatAdapter = TextAdapter(mutableListOf())
        val curhatRecyclerView: RecyclerView = rootView.findViewById(R.id.recent_post_rview)
        curhatRecyclerView.adapter = curhatAdapter
        curhatRecyclerView.layoutManager=LinearLayoutManager(this.activity)
        val replyAdapter = TextAdapter(mutableListOf())
        val replyRecyclerView: RecyclerView = rootView.findViewById(R.id.recent_reply_rview)
        replyRecyclerView.adapter = replyAdapter
        replyRecyclerView.layoutManager=LinearLayoutManager(this.activity)



        val nameView : TextView = rootView.findViewById(R.id.user_profile_name)
        val emailView : TextView = rootView.findViewById(R.id.user_profile_email)

        nameView.setText(viewModel.userName)
        emailView.setText(viewModel.userEmail)


//        Log.i("Testing Info", )

        val curhatCountView : TextView = rootView.findViewById(R.id.total_post)
        val totalPostObserver = Observer<Int>{totalPostCount->

            if(totalPostCount==1){
                curhatCountView.setText(""+ totalPostCount + " curhat posted")
            }else{
                curhatCountView.setText(""+ totalPostCount + " curhats posted")
            }

        }

        val replyCountView : TextView = rootView.findViewById(R.id.total_reply)

        val totalReplyObserver = Observer<Int>{totalReplyCount->
            if(totalReplyCount==1){
                replyCountView.setText(""+ totalReplyCount + " reply posted")
            }else{
                replyCountView.setText(""+ totalReplyCount + " replies posted")
            }
        }


        viewModel.curhatCount.observe(this,totalPostObserver)
        viewModel.replyCount.observe(this,totalReplyObserver)




        val logOutButton: Button = rootView.findViewById(R.id.log_out_button)
        logOutButton.setOnClickListener {
            AuthUI.getInstance()
                    .signOut(this.requireActivity())
                    .addOnCompleteListener {
                        val intent  = Intent(this.activity, LoginActivity::class.java)
                        startActivity(intent)
                        this.activity?.finish()
                    }
        }

        viewModel.recentCurhats.observe(viewLifecycleOwner, Observer {curhats ->

            if(!curhats.isEmpty()){
                curhatAdapter.clearAllText()
            }
            for (curhat in curhats){
                curhatAdapter.addText(Text(curhat.content))
            }

        })

        viewModel.recentReplies.observe(viewLifecycleOwner, Observer {replies ->

            if(!replies.isEmpty()){
                replyAdapter.clearAllText()
            }
            for (reply in replies){
                replyAdapter.addText(Text(reply.content))
            }

        })


        return rootView

    }


}