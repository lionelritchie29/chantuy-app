package edu.bluejack20_2.chantuy.repositories

import android.net.Uri
import android.util.Log
import com.bumptech.glide.annotation.GlideModule
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import edu.bluejack20_2.chantuy.models.CurhatTopic
import edu.bluejack20_2.chantuy.models.User
import edu.bluejack20_2.chantuy.views.user_profile.UserProfileViewModel
import java.net.URL

class UserRepository {
    companion object{
        val COLLECTION_NAME = "users"

        fun getUserByEmail(email:String):Task<QuerySnapshot>{
            val db= Firebase.firestore
            val user = db.collection(COLLECTION_NAME).whereEqualTo("email",email)
            return user.get()
        }

        fun getUserByEmail(email: String,callback: (List<User>) -> Unit) {
            val db = FirebaseFirestore.getInstance()
            db.collection(UserRepository.COLLECTION_NAME).whereEqualTo("email",email).get()
                    .addOnSuccessListener {topicDocs ->
                        val users = mutableListOf<User>()
                        for (topicDoc in topicDocs) {
                            val user = topicDoc.toObject(User::class.java)
                            user.id = topicDoc.id
                            users.add(user)
                        }
                        callback(users)
                    }
        }


        fun setUser(id: String, email: String, name:String) {
            val db = FirebaseFirestore.getInstance()
            val user=db.collection(UserRepository.COLLECTION_NAME).document(id)
            user.set(
                    hashMapOf(
                            "email" to email,
                            "name" to name
                    )
            )
        }

        fun getUser(id: String):Task<DocumentSnapshot> {
            val db = FirebaseFirestore.getInstance()
            val user=db.collection(UserRepository.COLLECTION_NAME).document(id)
            return user.get()
        }

        fun getCurrentUserId(): String {
            return FirebaseAuth.getInstance().currentUser.uid
        }


        fun getCurrentUser(callback: (User?) -> Unit) {
            val db = FirebaseFirestore.getInstance()

            val currentUserId = getCurrentUserId()
            db.collection(COLLECTION_NAME).document(currentUserId).get()
                .addOnSuccessListener {
                    val user = it.toObject(User::class.java)
                    callback(user)
                }
        }

        fun getUserById(id: String):DocumentReference {
            val db = FirebaseFirestore.getInstance()
            val user=db.collection(UserRepository.COLLECTION_NAME).document(id)
            return user
        }
        fun getUserById(userId: String, callback: (User?) -> Unit) {
            val db = FirebaseFirestore.getInstance()

            db.collection(COLLECTION_NAME).document(userId).get()
                .addOnSuccessListener {
                    val user = it.toObject(User::class.java)
                    Log.i("UserRepository", userId)
                    Log.i("UserRepository", user.toString())
                    Log.i("UserRepository", it.toString())
                    callback(user)
                }
        }
        fun updateProfileImage(url: String){
            val currUser= FirebaseAuth.getInstance().currentUser

            currUser.updateProfile(userProfileChangeRequest {
                photoUri= Uri.parse(url)
            })
            getUserById(currUser.uid).set(hashMapOf("profileImageId" to url), SetOptions.merge())
        }

    }
}