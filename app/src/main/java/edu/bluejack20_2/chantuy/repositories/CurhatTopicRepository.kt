package edu.bluejack20_2.chantuy.repositories

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bluejack20_2.chantuy.models.CurhatTopic

class CurhatTopicRepository {
    companion object {
        fun getAll(getAllCallback: (List<CurhatTopic>) -> Unit) {
            val db = FirebaseFirestore.getInstance()

            db.collection("topics").get()
                .addOnSuccessListener {topicDocs ->
                    val topics = mutableListOf<CurhatTopic>()
                    for (topicDoc in topicDocs) {
                        val topic = topicDoc.toObject(CurhatTopic::class.java)
                        topic.id = topicDoc.id
                        topics.add(topic)
                    }
                    getAllCallback(topics)
                }
        }
    }
}