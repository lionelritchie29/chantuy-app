package edu.bluejack20_2.chantuy.models

import com.google.firebase.Timestamp

data class User (
    var id: String="",
    val name: String="Kosong",
    val age: Int?=1,
    val email:String?="",
    val password:String?="",
    val joinedAt: Timestamp?=null,
    val notification: List<Notification>?= null,
    val isAdmin: Boolean?=false,
    val profileImageId: String?=""
)