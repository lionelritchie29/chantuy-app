package edu.bluejack20_2.chantuy.views.notification

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import edu.bluejack20_2.chantuy.models.CurhatComment
import edu.bluejack20_2.chantuy.models.Notification
import edu.bluejack20_2.chantuy.repositories.CurhatCommentRepository
import edu.bluejack20_2.chantuy.repositories.CurhatRepository
import edu.bluejack20_2.chantuy.repositories.NotificationRepository
import edu.bluejack20_2.chantuy.repositories.UserRepository
import edu.bluejack20_2.chantuy.views.Text
import kotlin.math.ceil


class NotificationViewModel () {

    val nList: MutableLiveData<List<Notification>> = MutableLiveData<List<Notification>>().apply {
        value = mutableListOf()
    }

    var limit: Long = 10
    var listSize: Int = 0;
    var currSize:Int=0

    init {
        NotificationRepository.getNotifCount {
            listSize = it
            Log.i("NotifViewModel", listSize.toString())
            getData()
        }
    }

    fun getData(){
        val currentUserId = UserRepository.getCurrentUserId()
        NotificationRepository.getNotif(currentUserId, limit) {
            nList.value = it
            currSize = it.size
        }
    }
}