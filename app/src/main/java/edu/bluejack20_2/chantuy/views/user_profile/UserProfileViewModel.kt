package edu.bluejack20_2.chantuy.views.user_profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.models.CurhatComment
import edu.bluejack20_2.chantuy.models.CurhatTopic
import edu.bluejack20_2.chantuy.models.User
import edu.bluejack20_2.chantuy.repositories.CurhatCommentRepository
import edu.bluejack20_2.chantuy.repositories.CurhatRepository
import edu.bluejack20_2.chantuy.repositories.UserRepository

class UserProfileViewModel {
    var initCurhats : List<Curhat>? = listOf()
    var initReplies : List<CurhatComment>? = listOf()

    val userName: String = FirebaseAuth.getInstance().currentUser.displayName
    val userEmail: String = FirebaseAuth.getInstance().currentUser.email

    val curhatCount : MutableLiveData<Int> by lazy{
        MutableLiveData<Int>()
    }
    val replyCount : MutableLiveData<Int> by lazy{
        MutableLiveData<Int>()
    }

    var recentReplies: MutableLiveData<List<CurhatComment>> = MutableLiveData<List<CurhatComment>>().apply {
        postValue(initReplies)
    }

    var recentCurhats: MutableLiveData<List<Curhat>> = MutableLiveData<List<Curhat>>().apply {
        postValue(initCurhats)
    }


    constructor(){
        val currUserId=FirebaseAuth.getInstance().currentUser.uid
        CurhatRepository.countUserPost(currUserId).addOnCompleteListener{resultDoc->
            if(resultDoc.result!=null){
                curhatCount.value=resultDoc.result?.size()
            }
            else{
                curhatCount.value=0
            }
        }

        CurhatCommentRepository.countUserComment(currUserId) .addOnCompleteListener{resultDoc->
            if(resultDoc.result!=null){
                replyCount.value=resultDoc.result?.size()
            }
            else{
                replyCount.value=0
            }
        }
        CurhatRepository.userProfilePost(currUserId).addOnCompleteListener { profilePost ->
            recentCurhats.value=profilePost.result?.toObjects(Curhat::class.java)
        }

        CurhatCommentRepository.userProfilePost(currUserId).addOnCompleteListener { profilePost ->
            recentReplies.value=profilePost.result?.toObjects(CurhatComment::class.java)
        }






    }

    fun getRecentCurhats(){

    }


}