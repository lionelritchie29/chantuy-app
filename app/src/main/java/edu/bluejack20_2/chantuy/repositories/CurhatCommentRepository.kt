package edu.bluejack20_2.chantuy.repositories

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import edu.bluejack20_2.chantuy.models.CommentListDocument
import edu.bluejack20_2.chantuy.models.CurhatComment

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
    }
}