package edu.bluejack20_2.chantuy.models

data class User (
    val id: String,
    val name: String,
    val age: Int,
    val email:String,
    val password:String,
    val joinedAt: String,
    val notification: List<Notification>
)