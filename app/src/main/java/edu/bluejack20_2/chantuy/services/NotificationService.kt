package edu.bluejack20_2.chantuy.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.models.GLOBALS

class NotificationService: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var appSettingPreferences: SharedPreferences? = context?.getSharedPreferences(GLOBALS.SETTINGS_PREFERENCES_NAME, 0)
        val isNotificationOn = appSettingPreferences?.getBoolean(GLOBALS.SETTINGS_NOTIFICATION_KEY, false)

        if (isNotificationOn!!) {
            val builder =
                NotificationCompat.Builder(context!!, GLOBALS.NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_cool_icon)
                    .setContentTitle("Chantuy App")
                    .setContentText("Hi, we missed you!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val notificationManager = NotificationManagerCompat.from(context!!)
            notificationManager.notify(200, builder!!.build())
            Log.i("NotificationService", "Alarm Called!")
        }
    }

}