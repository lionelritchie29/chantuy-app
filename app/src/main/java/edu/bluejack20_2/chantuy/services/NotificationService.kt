package edu.bluejack20_2.chantuy.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import edu.bluejack20_2.chantuy.R

class NotificationService: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val builder =
            NotificationCompat.Builder(context!!, "reminder")
                .setSmallIcon(R.drawable.ic_cool_icon)
                .setContentTitle("Chantuy App")
                .setContentText("Hi, we missed you!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(context!!)
        notificationManager.notify(200, builder!!.build())
        Log.i("NotificationService", "Alarm Called!")
        Toast.makeText(context!!, "Notification Called!", Toast.LENGTH_SHORT).show()
    }

}