package edu.bluejack20_2.chantuy.repositories

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.utils.CurhatViewUtil
import java.util.*
import kotlin.random.Random


class CurhatRepository {
    companion object {
        fun getAll(getAllCallback: (List<Curhat>) -> Unit) {
            val db = FirebaseFirestore.getInstance()

            db.collection("curhats").get()
                    .addOnSuccessListener {curhatDocs ->
                        val curhats = mutableListOf<Curhat>()
                        for (curhatDoc in curhatDocs) {
                            val curhat = curhatDoc.toObject(Curhat::class.java)
                            curhat.id = curhatDoc.id
                            curhats.add(curhat)
                        }
                        getAllCallback(curhats)
                    }
        }

        fun addDummy() {
            val data1 = hashMapOf(
                    "content" to "Hari ini adalah hari yang menyenangkan, aku baru saja dapat pacar baru",
                    "likeCount" to Random.nextInt(5, 50),
                    "dislikeCount" to Random.nextInt(0, 50),
                    "viewCount" to Random.nextInt(5, 100),
                    "topic" to "topics/4dX2GpFcubGlAFNVTnRW",
                    "user" to "users/9xktLUHQWHXQ1wrWOTw0",
                    "createdAt" to Timestamp.now(),
                    "updatedAt" to Timestamp.now()
            )

            val data2 = hashMapOf(
                    "content" to "LL dan JP sedang mengerjakan TPA mobile tentang curhat curhat gitu deh keren bingitss",
                    "likeCount" to Random.nextInt(5, 50),
                    "dislikeCount" to Random.nextInt(0, 50),
                    "viewCount" to Random.nextInt(5, 100),
                    "topic" to "topics/4dX2GpFcubGlAFNVTnRW",
                    "user" to "users/9xktLUHQWHXQ1wrWOTw0",
                    "createdAt" to Timestamp.now(),
                    "updatedAt" to Timestamp.now()
            )

            val data3 = hashMapOf(
                    "content" to "Gaada apa yang mau jalan jalan sama gua dimasa pandemi ini gua stress bangettt guess sumpah :(",
                    "likeCount" to Random.nextInt(5, 50),
                    "dislikeCount" to Random.nextInt(0, 50),
                    "viewCount" to Random.nextInt(5, 100),
                    "topic" to "topics/4dX2GpFcubGlAFNVTnRW",
                    "user" to "users/9xktLUHQWHXQ1wrWOTw0",
                    "createdAt" to Timestamp.now(),
                    "updatedAt" to Timestamp.now()
            )

            val db = FirebaseFirestore.getInstance()
            db.collection("curhats").add(data1)
            db.collection("curhats").add(data2)
            db.collection("curhats").add(data3)
        }
    }
}