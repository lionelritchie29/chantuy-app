package edu.bluejack20_2.chantuy.views.user_profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.models.CurhatTopic
import edu.bluejack20_2.chantuy.models.User
import edu.bluejack20_2.chantuy.repositories.CurhatRepository
import edu.bluejack20_2.chantuy.repositories.UserRepository

class UserProfileViewModel {
    val currUserQuery:Task<QuerySnapshot> = UserRepository.getUserByEmail("johanespeter.jp@gmail.com")
    val initCurhats : List<Curhat> = listOf()
    val curhatCount : MutableLiveData<Int> by lazy{
        MutableLiveData<Int>()
    }
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

            val users = userDoc.toObjects(User::class.java)
                if(users.size==1){
                    val user=users.get(0)
                    for(userDoc in userDoc){

                        user.id=userDoc.id
                    }
                    currUser.value=user

                }


            val currUserPostQuery:Task<QuerySnapshot> = CurhatRepository.countUserPost(""+currUser.value?.id)
            currUserPostQuery.addOnSuccessListener { userPost ->
                curhatCount.value=userPost.size()

            }
        }
    }

}