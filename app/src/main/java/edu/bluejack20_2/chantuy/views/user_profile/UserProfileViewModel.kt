package edu.bluejack20_2.chantuy.views.user_profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.models.User
import edu.bluejack20_2.chantuy.repositories.UserRepository

class UserProfileViewModel {
    val currUserQuery:Task<QuerySnapshot> = UserRepository.getUserByEmail("johanespeter.jp@gmail.com")
//    var currUser: User?=null
    val initCurhats : List<Curhat> = listOf(
        Curhat("1", "Test1", 1, 2, 3, null, null, null),
        Curhat("2", "Test2", 1, 2, 3, null, null, null),
        Curhat("3", "Test3", 1, 2, 3, null, null, null)
    )
    var recentCurhats: MutableLiveData<List<Curhat>> = MutableLiveData<List<Curhat>>().apply {
        postValue(initCurhats)
    }
    val currUser: MutableLiveData<User> by lazy{
        MutableLiveData<User>()
    }
    constructor(){
        getUser()
    }
    fun getRecentCurhats(){

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