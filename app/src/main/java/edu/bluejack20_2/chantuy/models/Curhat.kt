package edu.bluejack20_2.chantuy.models

import com.google.firebase.Timestamp


data class Curhat (
    var id: String = "",
//    var topic: CurhatTopic,
    //var user: User,
    var content: String = "",
    var likeCount: Int = 0,
    var dislikeCount: Int = 0,
    var viewCount: Int = 0,
    // var comments: Comments
    var createdAt:  Timestamp? = null,
    var updatedAt: Timestamp? = null
)