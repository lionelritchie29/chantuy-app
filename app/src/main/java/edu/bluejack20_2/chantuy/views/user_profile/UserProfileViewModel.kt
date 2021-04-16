package edu.bluejack20_2.chantuy.views.user_profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import edu.bluejack20_2.chantuy.models.User
import edu.bluejack20_2.chantuy.repositories.UserRepository

class UserProfileViewModel {
    val currUserQuery:Task<QuerySnapshot> = UserRepository.getUserByEmail("johanespeter.jp@gmail.com")
//    var currUser: User?=null

    val currUser: MutableLiveData<User> by lazy{
        MutableLiveData<User>()
    }
    constructor(){
        getUser()
    }
    fun getRecentPost(){

    }
    fun getUser(){
        currUserQuery.addOnSuccessListener {userDoc->
            Log.i("Testing",""+userDoc.size())
            val users = userDoc.toObjects(User::class.java)
                if(users.size==1){
                    currUser.value=users.get(0)
                }
        }


    }

}