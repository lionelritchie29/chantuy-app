package edu.bluejack20_2.chantuy.repositories

import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
                    .startAt(curhat.createdAt, curhat.viewCount).limit(5).get()
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
                    .startAt(curhat.viewCount).limit(5)
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

