package edu.bluejack20_2.chantuy.views.notification

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import edu.bluejack20_2.chantuy.models.CurhatComment
import edu.bluejack20_2.chantuy.repositories.CurhatCommentRepository
import edu.bluejack20_2.chantuy.repositories.CurhatRepository
import edu.bluejack20_2.chantuy.repositories.NotificationRepository
import edu.bluejack20_2.chantuy.repositories.UserRepository
import edu.bluejack20_2.chantuy.views.Text
import kotlin.math.ceil


class NotificationViewModel () {

    val nList: MutableLiveData<MutableList<Text>> = MutableLiveData<MutableList<Text>>().apply {
        value = mutableListOf()
    }
    var totalData=0
    var totalPage=0
    var page=0
    var isLoading=false
    var lastTimeStamp: Timestamp =Timestamp.now()
    init {
        NotificationRepository.getNotif(UserRepository.getCurrentUser().uid).get().addOnSuccessListener {
            totalPage= ceil(it.size().toDouble()/5.toDouble()).toInt()
            totalData= it.size()
        }

    }
    fun getData(){

        if(page==totalPage)return
        if(isLoading)return
        isLoading=true
        page++
        NotificationRepository.getNotif(UserRepository.getCurrentUser().uid,lastTimeStamp,page==0).get().addOnSuccessListener {
            var flag=0

            Log.i("Testing",""+ it.size())
            for (i in it){
                flag++

                lastTimeStamp= i["createdAt"] as Timestamp
                CurhatCommentRepository.getComment(i["commentId"].toString()).addOnSuccessListener {ct->

                    UserRepository.getUserById(ct["user"].toString()).get().addOnSuccessListener {ut->
                        val temp=ut["name"].toString()+" commented on your curhat: " +ct["content"].toString()

                        nList.value?.add(Text(temp))
                        if(flag==5||nList.value?.size==totalData){
                            nList.value=nList.value
                            isLoading=false
                        }
                    }
                }
            }
        }
    }



}