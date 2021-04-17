package edu.bluejack20_2.chantuy.models

import com.google.firebase.Timestamp

data class User (
    val id: String="",
    val Name: String="Kosong",
    val Age: Int?=1,
    val Email:String?="",
    val Password:String?="",
    val JoinedAt: Timestamp?=null,
    val Notification: List<Notification>?= null,
    val IsAdmin: Boolean?=false,
    val ProfileImageId: String?=""
)