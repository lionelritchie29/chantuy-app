package edu.bluejack20_2.chantuy.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp


data class Curhat (
        @DocumentId var id: String = "",
        var topic: String = "",
        var user: String = "",
        var content: String = "",
        var likeCount: Int = 0,
        var dislikeCount: Int = 0,
        var viewCount: Int = 0,
        var isAnonymous: Boolean = true,
        @ServerTimestamp var createdAt:  Timestamp? = null,
        @ServerTimestamp var updatedAt: Timestamp? = null
)