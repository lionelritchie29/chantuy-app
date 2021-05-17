package edu.bluejack20_2.chantuy.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import edu.bluejack20_2.chantuy.views.curhat_detail.CurhatDetailActivity

class CurhatUtil {
    companion object {
        fun moveToCurhatDetail(id: String, context: Context) {
            val intent = Intent(context, CurhatDetailActivity::class.java)
            val b = Bundle()
            b.putString("id", id);
            intent.putExtras(b)
            context.startActivity(intent)
        }
    }
}