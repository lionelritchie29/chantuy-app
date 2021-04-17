package edu.bluejack20_2.chantuy.repositories

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.models.User
import edu.bluejack20_2.chantuy.models.CurhatTopic
import edu.bluejack20_2.chantuy.utils.CurhatViewUtil
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

            val db = FirebaseFirestore.getInstance()
            db.collection("curhats").add(data1)
            db.collection("curhats").add(data2)
            db.collection("curhats").add(data3)
        }
        fun countUserPost(id:String): Task<QuerySnapshot> {
            val db= Firebase.firestore
            val curhats = db.collection("curhats").whereEqualTo("user","users/"+id)
            Log.d("Testing","users/"+id)

            return curhats.get()
        }

    }

}

