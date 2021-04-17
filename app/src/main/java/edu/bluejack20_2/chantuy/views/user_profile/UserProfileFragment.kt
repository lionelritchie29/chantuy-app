package edu.bluejack20_2.chantuy.views.user_profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.models.User
import edu.bluejack20_2.chantuy.views.CurhatAdapter


class UserProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_user_profile, container, false)

        val viewModel = UserProfileViewModel()

        viewModel.getUser()
//        val recyclerView: RecyclerView = rootView.findViewById(R.id.curhat_recycler_view)


        val curhatAdapter = CurhatAdapter()
        val curhatRecyclerView: RecyclerView = rootView.findViewById(R.id.recent_post_rview)
        curhatRecyclerView.adapter = curhatAdapter


        val nameView : TextView = rootView.findViewById(R.id.user_profile_name)
        val currUserObserver = Observer<User>{newUser->
            nameView.setText(newUser.name)
        }
        viewModel.currUser.observe(this,currUserObserver)


        val curhatCountView : TextView = rootView.findViewById(R.id.total_post)

        val totalPostObserver = Observer<Int>{totalPostCount->
            curhatCountView.setText(""+ totalPostCount + " curhat(s) posted")
        }
        viewModel.curhatCount.observe(this,totalPostObserver)

//
//
//        viewModel.recentCurhats.observe(viewLifecycleOwner, Observer {curhats ->
//            curhatAdapter.submitList(curhats)
//        })



        return rootView

    }


}