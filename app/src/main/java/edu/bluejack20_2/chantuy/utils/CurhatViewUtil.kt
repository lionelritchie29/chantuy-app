package edu.bluejack20_2.chantuy.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class CurhatViewUtil {
    companion object {
        fun formatDate(timestamp: Timestamp?) : String {
            val sdf = SimpleDateFormat("dd-MM-yyyy 'at' HH:mm:ss")
            var formatted: String = ""

            if (timestamp != null) {
                formatted = sdf.format(timestamp?.toDate())
            }
            return formatted
        }
    }
}