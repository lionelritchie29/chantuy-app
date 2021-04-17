package edu.bluejack20_2.chantuy.repositories

import android.util.Log
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import edu.bluejack20_2.chantuy.models.User
import edu.bluejack20_2.chantuy.views.user_profile.UserProfileViewModel

class UserRepository {
    companion object{


//        fun getUserByEmail(email:String, vm: UserProfileViewModel){
//
//            val db= Firebase.firestore
//            var currUser:User?=null
//
//
//            val user = db.collection("users").whereEqualTo("Email",email)
//            user.get().addOnSuccessListener { userDoc ->
//                    val users = userDoc.toObjects<User>()
//
//                    if(users.size==1){
//
//                        vm.currUser=users.get(0)
//                    }
//
//            }
//
//        }
//        fun getUserByEmail(email:String):User?{
//            val db= Firebase.firestore
//            var currUser:User?=null
//            val user = db.collection("users").whereEqualTo("email",email)
//            user.get().addOnSuccessListener { userDoc ->
//                val users = userDoc.toObjects<User>()
//                if(users.size==1){
//                    currUser=users.get(0)
//                }
//            }
//            return currUser
//        }
        fun getUserByEmail(email:String):Task<QuerySnapshot>{
            val db= Firebase.firestore
            var currUser:User?=null

            val user = db.collection("users").whereEqualTo("Email",email)
            return user.get()
        }

    }

}