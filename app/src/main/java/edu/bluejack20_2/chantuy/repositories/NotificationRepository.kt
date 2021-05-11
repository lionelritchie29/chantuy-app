package edu.bluejack20_2.chantuy.repositories

import com.google.firebase.firestore.FirebaseFirestore
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
    }
}