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

            getCommentsById(curhatId) { commentList ->
                val comment = CurhatComment(userId, content, Timestamp.now(), Timestamp.now());
                if (commentList != null) {
                    val mutableCommentList = commentList.toMutableList()
                    mutableCommentList.add(comment)
                    db.collection(COLLECTION_NAME).document(curhatId)
                        .set(hashMapOf("comments" to mutableCommentList))
                        .addOnSuccessListener {
                            callback()
                        }
                } else {
                    val newCommentList = listOf(comment)
                    db.collection(COLLECTION_NAME).document(curhatId)
                        .set(hashMapOf("comments" to newCommentList))
                        .addOnSuccessListener {
                            callback()
                        }
                }
            }
        }

        fun getCommentsById(curhatId: String, callback : (List<CurhatComment>?) -> Unit) {
            val db = FirebaseFirestore.getInstance()

            db.collection(COLLECTION_NAME).document(curhatId).get()
                .addOnSuccessListener { commentDocs ->
                    val container =  commentDocs.toObject(CommentListDocument::class.java)
                    Log.i("CurhatCommentRepository", container.toString())
                    callback(container?.comments)
                }
        }
        fun countUserComment(id: String): Task<QuerySnapshot> {
            val db = Firebase.firestore
            val curhatReplies = db.collection(COLLECTION_NAME).whereEqualTo("user",  id)
            return curhatReplies.get()
        }
        fun userProfilePost(id: String): Task<QuerySnapshot> {
            val db = Firebase.firestore
            val curhats = db.collection(COLLECTION_NAME).whereEqualTo("user", id)
                .orderBy("createdAt", Query.Direction.ASCENDING).limit(3)
            return curhats.get()
        }
        fun getCommentCount(curhatId: String, callback: (Int) -> Unit) {
            val db = FirebaseFirestore.getInstance()

            db.collection(COLLECTION_NAME).document(curhatId).get()
                .addOnSuccessListener { commentdocs ->
                    val container = commentdocs.toObject(CommentListDocument::class.java)
                    if (container != null) {
                        callback(container.comments.size)
                    } else {
                        callback(0)
                    }
                }
        }

        fun deleteAllById(curhatId: String, callback: () -> Unit) {
            val db = FirebaseFirestore.getInstance()

            db.collection(COLLECTION_NAME).document(curhatId).delete()
                .addOnSuccessListener { callback() }
        }
    }
}