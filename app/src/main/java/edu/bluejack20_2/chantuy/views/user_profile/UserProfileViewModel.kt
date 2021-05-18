package edu.bluejack20_2.chantuy.views.user_profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bluejack20_2.chantuy.models.*
import edu.bluejack20_2.chantuy.repositories.CurhatCommentRepository
import edu.bluejack20_2.chantuy.repositories.CurhatRepository
import edu.bluejack20_2.chantuy.repositories.UserRepository

class UserProfileViewModel {
    var initCurhats: List<Curhat>? = listOf()
    var initReplies: List<CurhatComment>? = listOf()

    val curhatCount: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val replyCount: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    var recentReplies: MutableLiveData<List<CurhatComment>> =
        MutableLiveData<List<CurhatComment>>().apply {
            postValue(initReplies)
        }

    var recentCurhats: MutableLiveData<List<Curhat>> = MutableLiveData<List<Curhat>>().apply {
        postValue(initCurhats)
    }

    val currUser = FirebaseAuth.getInstance().currentUser

    constructor() {
        CurhatRepository.countUserPost(currUser.uid).addSnapshotListener { value, e ->
            if (e != null) {
                curhatCount.value = 0
                return@addSnapshotListener
            }
            if (value != null) {
                curhatCount.value = value?.size()
            } else {
                curhatCount.value = 0
            }
        }

        CurhatCommentRepository.countUserComment(currUser.uid).addSnapshotListener { value, e ->
            if (e != null) {
                replyCount.value = 0

                return@addSnapshotListener
            }
            if (value != null) {
                replyCount.value = value.size()
            } else {
                replyCount.value = 0
            }
        }

        getRecentCurhats()
        getRecentReplies()
    }

    fun getRecentCurhats() {
        CurhatRepository.userProfilePost(currUser.uid).addSnapshotListener { value, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            recentCurhats.value = value?.toObjects(Curhat::class.java)
        }
    }

    fun getRecentReplies() {
        CurhatCommentRepository.userProfilePost(currUser.uid).addSnapshotListener { value, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            recentReplies.value = value?.toObjects(CurhatComment::class.java)
        }
    }

    fun uploadImage() {

    }
}