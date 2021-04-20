package edu.bluejack20_2.chantuy.repositories

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bluejack20_2.chantuy.models.CommentListDocument
import edu.bluejack20_2.chantuy.models.CurhatComment
import java.lang.StringBuilder

class CurhatCommentRepository {
    companion object {
        private val COLLECTION_NAME = "comments"

        fun addComment(curhatId: String, userId: String, content: String, callback: () -> Unit) {
            val db = FirebaseFirestore.getInstance()

            val comment =
                CurhatComment("", curhatId, userId, content, Timestamp.now(), Timestamp.now())
            db.collection(COLLECTION_NAME).add(comment)
                .addOnSuccessListener {
                    callback()
                }
        }

        fun getCommentsByCurhatId(curhatId: String, callback: (List<CurhatComment>?) -> Unit) {
            val db = FirebaseFirestore.getInstance()

            db.collection(COLLECTION_NAME).whereEqualTo("curhatId", curhatId)
                .orderBy("createdAt", Query.Direction.DESCENDING).get()
                .addOnSuccessListener { it ->
                    val comments = mutableListOf<CurhatComment>()
                    for (doc in it.documents) {
                        val comment = doc.toObject(CurhatComment::class.java)
                        comments.add(comment!!)
                    }
                    callback(comments)
                }
        }

        fun deleteById(commentId: String, callback: () -> Unit)  {
            val db = FirebaseFirestore.getInstance()

            db.collection(COLLECTION_NAME).document(commentId).delete()
                .addOnSuccessListener {
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
            val curhats = db.collection(COLLECTION_NAME).whereEqualTo("user", id)
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