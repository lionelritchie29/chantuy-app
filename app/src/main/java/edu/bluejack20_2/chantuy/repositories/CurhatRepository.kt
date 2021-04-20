package edu.bluejack20_2.chantuy.repositories

import android.util.Log
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.models.CurhatComment
import edu.bluejack20_2.chantuy.models.User
import edu.bluejack20_2.chantuy.models.CurhatTopic
import edu.bluejack20_2.chantuy.utils.CurhatViewUtil
import org.w3c.dom.Comment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.random.Random


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
                    0,
                    0,
                    0,
                    isAnon,
                    Timestamp.now(),
                    Timestamp.now()
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

        fun incrementViewCount(curhatId: String) {
            val db = FirebaseFirestore.getInstance()
            db.collection(COLLECTION_NAME).document(curhatId)
                .update("viewCount", FieldValue.increment(1))
        }

        fun addDummy() {
            val data1 = hashMapOf(
                "content" to "Hari ini adalah hari yang menyenangkan, aku baru saja dapat pacar baru",
                "user" to "user_id",
                "likeCount" to Random.nextInt(5, 50),
                "dislikeCount" to Random.nextInt(0, 50),
                "viewCount" to Random.nextInt(5, 100),
                "topic" to "4dX2GpFcubGlAFNVTnRW",
                "user" to "9xktLUHQWHXQ1wrWOTw0",
                "isAnonymous" to true,
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )

            val data2 = hashMapOf(
                "content" to "LL dan JP sedang mengerjakan TPA mobile tentang curhat curhat gitu deh keren bingitss",
                "user" to "user_id",
                "likeCount" to Random.nextInt(5, 50),
                "dislikeCount" to Random.nextInt(0, 50),
                "viewCount" to Random.nextInt(5, 100),
                "topic" to "4dX2GpFcubGlAFNVTnRW",
                "user" to "9xktLUHQWHXQ1wrWOTw0",
                "isAnonymous" to false,
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )

            val data3 = hashMapOf(
                "content" to "Gaada apa yang mau jalan jalan sama gua dimasa pandemi ini gua stress bangettt guess sumpah :(",
                "user" to "user_id",
                "likeCount" to Random.nextInt(5, 50),
                "dislikeCount" to Random.nextInt(0, 50),
                "viewCount" to Random.nextInt(5, 100),
                "topic" to "4dX2GpFcubGlAFNVTnRW",
                "user" to "9xktLUHQWHXQ1wrWOTw0",
                "isAnonymous" to true,
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )

            val db = FirebaseFirestore.getInstance()
            db.collection("curhats").add(data1).addOnSuccessListener { curhat ->
                val comments: List<CurhatComment> = listOf(
                    CurhatComment(
                        "user_id",
                        "Mana ada menyenangkan broder",
                        Timestamp.now(),
                        Timestamp.now()
                    ),
                    CurhatComment(
                        "user_id",
                        "Menyenangkan dong kawanku",
                        Timestamp.now(),
                        Timestamp.now()
                    ),
                    CurhatComment(
                        "user_id",
                        "Ga Menyenangkan dong kawanku",
                        Timestamp.now(),
                        Timestamp.now()
                    ),
                    CurhatComment(
                        "user_id",
                        "Brandon penyelamat hidup",
                        Timestamp.now(),
                        Timestamp.now()
                    ),
                    CurhatComment(
                        "user_id",
                        "Clar otw jadian sama fs",
                        Timestamp.now(),
                        Timestamp.now()
                    )
                )

                db.collection("comments").document(curhat.id).set(
                    hashMapOf(
                        "comments" to comments
                    )
                )
            }

            db.collection("curhats").add(data2).addOnSuccessListener { curhat ->
                val comments: List<CurhatComment> = listOf(
                    CurhatComment(
                        "user_id",
                        "TPA game adalah jalan hidupku",
                        Timestamp.now(),
                        Timestamp.now()
                    ),
                    CurhatComment("user_id", "TPA desktop skip", Timestamp.now(), Timestamp.now()),
                    CurhatComment(
                        "user_id",
                        "TPA web buset nguli imba",
                        Timestamp.now(),
                        Timestamp.now()
                    ),
                    CurhatComment(
                        "user_id",
                        "TPA mobile lg kerjain skrg",
                        Timestamp.now(),
                        Timestamp.now()
                    ),
                    CurhatComment(
                        "user_id",
                        "TPA jarkom gangerti",
                        Timestamp.now(),
                        Timestamp.now()
                    )
                )

                db.collection("comments").document(curhat.id).set(
                    hashMapOf(
                        "comments" to comments
                    )
                )
            }

            db.collection("curhats").add(data3).addOnSuccessListener { curhat ->
                val comments: List<CurhatComment> = listOf(
                    CurhatComment(
                        "user_id",
                        "Jalan - jalan mulu",
                        Timestamp.now(),
                        Timestamp.now()
                    ),
                    CurhatComment(
                        "user_id",
                        "yee suka2 TS nya dong",
                        Timestamp.now(),
                        Timestamp.now()
                    )
                )

                db.collection("comments").document(curhat.id).set(
                    hashMapOf(
                        "comments" to comments
                    )
                )
            }
        }

        fun countUserPost(id: String): Query {
            val db = Firebase.firestore
            val curhats = db.collection(COLLECTION_NAME).whereEqualTo("user", id)
            return curhats
        }



        fun userProfilePost(id: String): Query {
            val db = Firebase.firestore
            val curhats = db.collection(COLLECTION_NAME).whereEqualTo("user", id).orderBy("createdAt", Query.Direction.ASCENDING).limit(3)
            return curhats
        }


    }

}

