package edu.bluejack20_2.chantuy.utils

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import com.google.firebase.Timestamp
import edu.bluejack20_2.chantuy.R
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

        fun changeReactionBtnColor(btn: ImageButton, colorId: Int, view: View) {
            btn.setColorFilter(
                ContextCompat.getColor(view.context, colorId),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
        }
    }
}