package edu.bluejack20_2.chantuy.models

import java.util.*

data class CurhatComment (
    var id: String,
    //var user: User,
    var content: String,
    var createdAt: Date,
    var updatedAt: Date,
    var deletedAt: Date
)