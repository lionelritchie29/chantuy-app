package edu.bluejack20_2.chantuy.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.local.QueryResult
import edu.bluejack20_2.chantuy.models.CommentNotification

class NotificationRepository {
    companion object {
        private val COLLECTION_NAME = "notifications"

        fun addNotif(commentId: String, curhatPosterUserId: String, callback: () -> Unit) {
            val db = FirebaseFirestore.getInstance()

            val notif = CommentNotification("", curhatPosterUserId, commentId)
            db.collection(COLLECTION_NAME).add(notif)
                .addOnSuccessListener {
                    callback()
                }
        }
        //curhatPosterUserId
        fun getNotif(userId: String, sIdx: Timestamp, isFirst: Boolean): Query {
            val db = FirebaseFirestore.getInstance()
            if(isFirst){
                return db.collection(COLLECTION_NAME).orderBy("createdAt",Query.Direction.DESCENDING).whereEqualTo("curhatPosterUserId",userId).limit(5)
            }
            else{
                return db.collection(COLLECTION_NAME).orderBy("createdAt",Query.Direction.DESCENDING).whereEqualTo("curhatPosterUserId",userId).startAt(sIdx).limit(5)
            }
        }



        fun getNotif(userId: String): Query {
            val db = FirebaseFirestore.getInstance()
            return db.collection(COLLECTION_NAME).orderBy("createdAt",Query.Direction.DESCENDING).whereEqualTo("curhatPosterUserId",userId)
        }

    }

}