package edu.bluejack20_2.chantuy.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import edu.bluejack20_2.chantuy.utils.NotificationUtil
import java.text.SimpleDateFormat
import java.util.*

class MyFirebaseMessagingServices : FirebaseMessagingService() {
    private val TAG = "MessagingServices"
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "Message data payload: ${remoteMessage.data}")


        // get message
        val title = remoteMessage.data["title"]
        val message = remoteMessage.data["message"]
        val isScheduled = remoteMessage.data["isScheduled"] == "true"

        if (isScheduled) {
            // This is Scheduled Notification, Schedule it
            val scheduledTime = remoteMessage.data["scheduledTime"]
            scheduleAlarm(scheduledTime, title, message)
        } else {
            // This is not scheduled notification, show it now
            showNotification(title!!, message!!)
        }
    }

    private fun scheduleAlarm(
        scheduledTimeString: String?,
        title: String?,
        message: String?
    ) {
        val alarmMgr = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent =
            Intent(applicationContext, NotificationBroadcastReceiver::class.java).let { intent ->
                intent.putExtra("NOTIFICATION_TITLE", title)
                intent.putExtra("NOTIFICATION_MESSAGE", message)
                PendingIntent.getBroadcast(applicationContext, 0, intent, 0)
            }

        // Parse Schedule time
        val scheduledTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            .parse(scheduledTimeString!!)

        scheduledTime?.let {
            // With set(), it'll set non repeating one time alarm.
            alarmMgr.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                it.time + (60 * 100),
                AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                alarmIntent
            )
        }
    }

    private fun showNotification(title: String, message: String) {
        NotificationUtil(applicationContext).showNotification(title, message)
    }
}