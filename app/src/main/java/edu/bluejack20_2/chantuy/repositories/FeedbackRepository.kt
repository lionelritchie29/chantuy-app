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
    }
}