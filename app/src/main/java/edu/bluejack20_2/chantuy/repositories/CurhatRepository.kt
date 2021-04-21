package edu.bluejack20_2.chantuy.repositories

import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bluejack20_2.chantuy.utils.CurhatViewUtil
import org.w3c.dom.Comment
import java.text.SimpleDateFormat
import java.util.*
import edu.bluejack20_2.chantuy.models.*
import kotlin.collections.HashMap


class CurhatRepository {
    companion object {
        private val COLLECTION_NAME = "curhats"

        fun getNewestCurhat(curhat: Curhat?, callback: (List<Curhat>) -> Unit) {
            val db = FirebaseFirestore.getInstance()

            if (curhat == null) {
                db.collection(COLLECTION_NAME)
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .orderBy("viewCount", Query.Direction.DESCENDING)
                    .limit(5).get()
                    .addOnSuccessListener { curhatDocs ->
                        val curhats = mutableListOf<Curhat>()
                        for (curhatDoc in curhatDocs) {
                            val curhat = curhatDoc.toObject(Curhat::class.java)
                            curhat.id = curhatDoc.id
                            curhats.add(curhat)
                        }
                        callback(curhats)
                    }
            } else {
                db.collection(COLLECTION_NAME)
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .orderBy("viewCount", Query.Direction.DESCENDING)
                    .startAfter(curhat.createdAt, curhat.viewCount).limit(5).get()
                    .addOnSuccessListener { curhatDocs ->
                        val curhats = mutableListOf<Curhat>()
                        for (curhatDoc in curhatDocs) {
                            val curhat = curhatDoc.toObject(Curhat::class.java)
                            curhat.id = curhatDoc.id
                            curhats.add(curhat)
                        }
                        callback(curhats)
                    }
            }
        }

        fun getHottestCurhat(curhat: Curhat?, callback: (List<Curhat>) -> Unit) {
            val db = FirebaseFirestore.getInstance()

            if (curhat == null) {
                db.collection(COLLECTION_NAME)
                    .orderBy("viewCount", Query.Direction.DESCENDING)
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .limit(5).get()
                    .addOnSuccessListener { curhatDocs ->
                        val curhats = mutableListOf<Curhat>()
                        for (curhatDoc in curhatDocs) {
                            val curhat = curhatDoc.toObject(Curhat::class.java)
                            curhat.id = curhatDoc.id
                            curhats.add(curhat)
                        }
                        callback(curhats)
                    }
            } else {
                db.collection(COLLECTION_NAME)
                    .orderBy("viewCount", Query.Direction.DESCENDING)
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .startAfter(curhat.viewCount).limit(5)
                    .get()
                    .addOnSuccessListener { curhatDocs ->
                        val curhats = mutableListOf<Curhat>()
                        for (curhatDoc in curhatDocs) {
                            val curhat = curhatDoc.toObject(Curhat::class.java)
                            curhat.id = curhatDoc.id
                            curhats.add(curhat)
                        }
                        callback(curhats)
                    }
            }
        }

        fun addCurhat(content: String, isAnon: Boolean, topicId: String, callback: () -> Unit) {
            val db = FirebaseFirestore.getInstance()

            val curhat =
                Curhat(
                    "",
                    topicId,
                    UserRepository.getCurrentUserId(),
                    content,
                    isAnonymous = isAnon,
                    createdAt = Timestamp.now(),
                    updatedAt = Timestamp.now()
                )
            db.collection(COLLECTION_NAME).add(curhat)
                .addOnSuccessListener { callback() }
        }

        fun getById(curhatId: String, callback: (Curhat) -> Unit) {
            val db = FirebaseFirestore.getInstance()

            db.collection(COLLECTION_NAME).document(curhatId).get()
                .addOnSuccessListener { curhatDoc ->
                    val curhat = curhatDoc.toObject(Curhat::class.java)
                    callback(curhat!!)
                }
        }

        fun getByTopic(topicId: String, callback: (List<Curhat>) -> Unit) {
            val db = FirebaseFirestore.getInstance()

            db.collection(COLLECTION_NAME).whereEqualTo("topic", topicId).get()
                .addOnSuccessListener { curhatDocs ->
                    val curhats = mutableListOf<Curhat>()
                    for (curhatDoc in curhatDocs) {
                        val curhat = curhatDoc.toObject(Curhat::class.java)
                        curhat.id = curhatDoc.id
                        curhats.add(curhat)
                    }
                    callback(curhats)
                }
        }

        fun getCurhatBySearch(searchString: String,mostLike: Boolean): Query {
            val db = FirebaseFirestore.getInstance()
            if(mostLike)return db.collection(COLLECTION_NAME).orderBy("likeCount",Query.Direction.DESCENDING)
            return db.collection(COLLECTION_NAME).orderBy("dislikeCount",Query.Direction.DESCENDING)
        }
      
        fun getCurhatBySearch(searchString: String, timeType: Int): Query {
            val db = FirebaseFirestore.getInstance()
            var dateReducer: Date
            if(timeType==0){
                dateReducer=Date("0000-00-01")
            }
            else if(timeType==1){
                dateReducer=Date("0000-00-07")
            }
            else if(timeType==2){
                dateReducer=Date("0000-01-00")
            }else{
                dateReducer=Date("0001-00-09")
            }
            var date= Date().time - dateReducer.time
            return db.collection(COLLECTION_NAME).whereGreaterThan("createdAt",date)
//            whereEqualTo("content",searchString)

        }

        fun update(curhatId: String, curhat: HashMap<String, Any>, callback: () -> Unit) {
            val db = FirebaseFirestore.getInstance()
            db.collection(COLLECTION_NAME).document(curhatId)
                .update(
                    "content",
                    curhat.get("content"),
                    "topic",
                    curhat.get("topic"),
                    "anonymous",
                    curhat.get("anonymous")
                )
                .addOnSuccessListener {
                    callback()
                }
        }

        fun deleteById(curhatId: String, callback: () -> Unit) {
            val db = FirebaseFirestore.getInstance()

            db.collection(COLLECTION_NAME).document(curhatId).delete()
                .addOnSuccessListener {
                    CurhatCommentRepository.deleteAllById(curhatId) {
                        callback()
                    }
                }
        }

        fun incrementCommentCount(curhatId: String) {
            val db = FirebaseFirestore.getInstance()
            db.collection(COLLECTION_NAME).document(curhatId)
                .update("commentCount", FieldValue.increment(1))
        }

        fun decrementCommentCount(curhatId: String) {
            val db = FirebaseFirestore.getInstance()
            db.collection(COLLECTION_NAME).document(curhatId)
                .update("commentCount", FieldValue.increment(-1))
        }

        fun incrementViewCount(curhatId: String) {
            val db = FirebaseFirestore.getInstance()
            db.collection(COLLECTION_NAME).document(curhatId)
                .update("viewCount", FieldValue.increment(1))
        }

        fun countUserPost(id: String): Query {
            val db = Firebase.firestore
            val curhats = db.collection(COLLECTION_NAME).whereEqualTo("user", id)
            return curhats
        }

        fun userProfilePost(id: String): Query {
            val db = Firebase.firestore
            val curhats = db.collection(COLLECTION_NAME).whereEqualTo("user", id)
                .orderBy("createdAt", Query.Direction.ASCENDING).limit(3)
            return curhats
        }
    }

}

