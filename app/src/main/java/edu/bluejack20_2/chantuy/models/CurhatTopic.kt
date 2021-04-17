package edu.bluejack20_2.chantuy.models

import com.google.firebase.firestore.DocumentId

data class CurhatTopic (
        @DocumentId var id: String = "",
        var name: String = "")