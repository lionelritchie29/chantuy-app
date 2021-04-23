package edu.bluejack20_2.chantuy.repositories

import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.chantuy.models.Feedback

class FeedbackRepository {
    companion object {
        private  val COLLECTION_NAME = "feedbacks"

        fun addFeedback(content: String, callback: () -> Unit) {
            val db = FirebaseFirestore.getInstance()

            val currentUserId = UserRepository.getCurrentUserId()
            val feedback = Feedback("", currentUserId, content)
            db.collection(COLLECTION_NAME).add(feedback)
                .addOnSuccessListener { callback() }
        }

        fun getAll(callback: (List<Feedback>) -> Unit) {
            val db = FirebaseFirestore.getInstance()

            db.collection(COLLECTION_NAME).orderBy("status").get()
                .addOnSuccessListener {
                    val feedbacks = mutableListOf<Feedback>()
                    for (doc in it.documents) {
                        feedbacks.add(doc.toObject(Feedback::class.java)!!)
                    }
                    callback(feedbacks.toList())
                }
        }
    }
}