package edu.bluejack20_2.chantuy.utils

import java.text.SimpleDateFormat
import java.util.*

class CurhatViewUtil {
    companion object {
        fun formatDate(date: Date) : String {
            val calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a")
            return sdf.format(calendar.time)
        }
    }
}