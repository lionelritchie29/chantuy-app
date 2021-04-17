package edu.bluejack20_2.chantuy.repositories

import android.util.Log
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.models.CurhatComment
import edu.bluejack20_2.chantuy.models.User
import edu.bluejack20_2.chantuy.models.CurhatTopic
import edu.bluejack20_2.chantuy.utils.CurhatViewUtil
import org.w3c.dom.Comment
import java.util.*
import kotlin.random.Random


class CurhatRepository {
    companion object {
        fun getAll(getAllCallback: (List<Curhat>) -> Unit) {
            val db = FirebaseFirestore.getInstance()

            db.collection("curhats").get()
                    .addOnSuccessListener { curhatDocs ->
                        val curhats = mutableListOf<Curhat>()
                        for (curhatDoc in curhatDocs) {
                            val curhat = curhatDoc.toObject(Curhat::class.java)
                            curhat.id = curhatDoc.id
                            curhats.add(curhat)
                        }
                        getAllCallback(curhats)
                    }
        }

        fun addCurhat(content: String, topicId: String, callback: () -> Unit) {
            val db = FirebaseFirestore.getInstance()

                val curhat = Curhat("", topicId, content, 0, 0, 0, Timestamp.now(), Timestamp.now())
                db.collection("curhats").add(curhat)
                        .addOnSuccessListener { callback() }
        }

        fun addDummy() {
            val data1 = hashMapOf(
                    "content" to "Hari ini adalah hari yang menyenangkan, aku baru saja dapat pacar baru",
                    "likeCount" to Random.nextInt(5, 50),
                    "dislikeCount" to Random.nextInt(0, 50),
                    "viewCount" to Random.nextInt(5, 100),
                    "topic" to "topics/4dX2GpFcubGlAFNVTnRW",
                    "user" to "users/9xktLUHQWHXQ1wrWOTw0",
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
            )

            val data2 = hashMapOf(
                    "content" to "LL dan JP sedang mengerjakan TPA mobile tentang curhat curhat gitu deh keren bingitss",
                    "likeCount" to Random.nextInt(5, 50),
                    "dislikeCount" to Random.nextInt(0, 50),
                    "viewCount" to Random.nextInt(5, 100),
                    "topic" to "topics/4dX2GpFcubGlAFNVTnRW",
                    "user" to "users/9xktLUHQWHXQ1wrWOTw0",
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
            )

            val data3 = hashMapOf(
                    "content" to "Gaada apa yang mau jalan jalan sama gua dimasa pandemi ini gua stress bangettt guess sumpah :(",
                    "likeCount" to Random.nextInt(5, 50),
                    "dislikeCount" to Random.nextInt(0, 50),
                    "viewCount" to Random.nextInt(5, 100),
                    "topic" to "topics/4dX2GpFcubGlAFNVTnRW",
                    "user" to "users/9xktLUHQWHXQ1wrWOTw0",
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
            )

            val mapper = jacksonObjectMapper()
            val db = FirebaseFirestore.getInstance()
            db.collection("curhats").add(data1).addOnSuccessListener { curhat ->
                val comments : List<CurhatComment> = listOf(
                    CurhatComment("", "Mana ada menyenangkan broder", Timestamp.now(), Timestamp.now()),
                    CurhatComment("", "Menyenangkan dong kawanku", Timestamp.now(), Timestamp.now()),
                    CurhatComment("", "Ga Menyenangkan dong kawanku", Timestamp.now(), Timestamp.now()),
                    CurhatComment("", "Brandon penyelamat hidup", Timestamp.now(), Timestamp.now()),
                    CurhatComment("", "Clar otw jadian sama fs", Timestamp.now(), Timestamp.now())
                )

                db.collection("comments").document(curhat.id).set(hashMapOf(
                    "comments" to comments
                ))
            }

            db.collection("curhats").add(data2).addOnSuccessListener { curhat ->
                val comments : List<CurhatComment> = listOf(
                    CurhatComment("", "TPA game adalah jalan hidupku", Timestamp.now(), Timestamp.now()),
                    CurhatComment("", "TPA desktop skip", Timestamp.now(), Timestamp.now()),
                    CurhatComment("", "TPA web buset nguli imba", Timestamp.now(), Timestamp.now()),
                    CurhatComment("", "TPA mobile lg kerjain skrg", Timestamp.now(), Timestamp.now()),
                    CurhatComment("", "TPA jarkom gangerti", Timestamp.now(), Timestamp.now())
                )

                db.collection("comments").document(curhat.id).set(hashMapOf(
                    "comments" to comments
                ))
            }

            db.collection("curhats").add(data3).addOnSuccessListener { curhat ->
                val comments : List<CurhatComment> = listOf(
                    CurhatComment("", "Jalan - jalan mulu", Timestamp.now(), Timestamp.now()),
                    CurhatComment("", "yee suka2 TS nya dong", Timestamp.now(), Timestamp.now())
                )

                db.collection("comments").document(curhat.id).set(hashMapOf(
                    "comments" to comments
                ))
            }
        }
        fun countUserPost(id:String): Task<QuerySnapshot> {
            val db= Firebase.firestore
            val curhats = db.collection("curhats").whereEqualTo("user","users/"+id)
            Log.d("Testing","users/"+id)

            return curhats.get()
        }

    }

}

