package edu.bluejack20_2.chantuy.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class User (
    @DocumentId var id: String="",
    val name: String="Kosong",
    var age: Int?=1,
    val email:String?="",
    val gender:String? = "",
    val password:String?="",
    val dateOfBirth: Timestamp? = null,
    val joinedAt: Timestamp?=null,
    var isAdmin: Boolean?=false,
    val profileImageId: String?=""

)