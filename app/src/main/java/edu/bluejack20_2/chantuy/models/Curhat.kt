package edu.bluejack20_2.chantuy.models

import java.util.*

data class Curhat (
    var id: String,
//    var topic: CurhatTopic,
    //var user: User,
    var content: String,
    var likeCount: Int,
    var dislikeCount: Int,
    var viewCount: Int,
    // var comments: Comments
    var createdAt: Date?,
    var updatedAt: Date?,
    var deletedAt: Date?
)