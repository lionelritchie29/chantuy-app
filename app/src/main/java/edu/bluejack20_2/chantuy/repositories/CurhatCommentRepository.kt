package edu.bluejack20_2.chantuy.repositories

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bluejack20_2.chantuy.models.CurhatComment

class CurhatCommentRepository {
    companion object {
        private val COLLECTION_NAME = "comments"

        fun addComment(curhatId: String, userId: String, content: String, callback: () -> Unit) {
            val db = FirebaseFirestore.getInstance()

            val comment =
                CurhatComment("", curhatId, userId, content, Timestamp.now(), Timestamp.now())
            db.collection(COLLECTION_NAME).add(comment)
                .addOnSuccessListener {
                    CurhatRepository.incrementCommentCount(curhatId)
                    callback()
                }
        }

        fun updateComment(commentId: String, content: String, callback: (String) -> Unit) {
            val db = FirebaseFirestore.getInstance()

            db.collection(COLLECTION_NAME).document(commentId)
                .update("content", content)
                .addOnSuccessListener {
                    callback(content)
                }
        }

        fun getCommentsByCurhatId(curhatId: String, callback: (List<CurhatComment>?) -> Unit) {
            val db = FirebaseFirestore.getInstance()

            db.collection(COLLECTION_NAME).whereEqualTo("curhatId", curhatId)
                .orderBy("createdAt").get()
                .addOnSuccessListener {
                    val comments = mutableListOf<CurhatComment>()
                    for (doc in it.documents) {
                        val comment = doc.toObject(CurhatComment::class.java)
                        comments.add(comment!!)
                    }
                    callback(comments)
                }
        }

        fun deleteById(curhatId: String, commentId: String, callback: () -> Unit)  {
            val db = FirebaseFirestore.getInstance()

            db.collection(COLLECTION_NAME).document(commentId).delete()
                .addOnSuccessListener {
                    CurhatRepository.decrementCommentCount(curhatId)
                    callback()
                }
        }

        fun countUserComment(id: String): Query {
            val db = Firebase.firestore
            val curhatReplies = db.collection(COLLECTION_NAME).whereEqualTo("user", id)
            return curhatReplies
        }

        fun userProfilePost(id: String): Query {
            val db = Firebase.firestore
            val curhats = db.collection(COLLECTION_NAME).whereEqualTo("user",  id)
                .orderBy("createdAt", Query.Direction.ASCENDING).limit(3)
            return curhats
        }

        fun getCommentCount(curhatId: String, callback: (Int) -> Unit) {
            val db = FirebaseFirestore.getInstance()
            db.collection(COLLECTION_NAME).whereEqualTo("curhatId", curhatId).get()
                .addOnSuccessListener {
                    callback(it.documents.size)
                }
        }

        fun deleteAllById(curhatId: String, callback: () -> Unit) {
            val db = FirebaseFirestore.getInstance()
            db.collection(COLLECTION_NAME).whereEqualTo("curhatId", curhatId).get()
                .addOnSuccessListener {
                    for (doc in it.documents) {
                        doc.reference.delete()
                    }
                    callback()
                }
        }


    }
}