package edu.bluejack20_2.chantuy.repositories

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.chantuy.models.CommentListDocument
import edu.bluejack20_2.chantuy.models.CurhatComment

class CurhatCommentRepository {
    companion object {
        private val COLLECTION_NAME = "comments"

        fun addComment(curhatId: String, userId: String, content: String) {
            val db = FirebaseFirestore.getInstance()

            val comment = CurhatComment(userId, content);
            db.collection(COLLECTION_NAME).document(curhatId)
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