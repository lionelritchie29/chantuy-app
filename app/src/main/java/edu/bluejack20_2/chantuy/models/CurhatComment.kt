package edu.bluejack20_2.chantuy.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class CurhatComment (
    @DocumentId val commentId: String = "",
    var curhatId: String = "",
    var user: String = "",
    var content: String = "",
    @ServerTimestamp var createdAt: Timestamp? = null,
    @ServerTimestamp var updatedAt: Timestamp ? = null
)