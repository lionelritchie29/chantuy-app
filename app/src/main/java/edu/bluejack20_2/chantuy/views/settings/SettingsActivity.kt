package edu.bluejack20_2.chantuy.views.settings

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import edu.bluejack20_2.chantuy.InsertFeedbackActivity
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.databinding.ActivitySettingsBinding
import edu.bluejack20_2.chantuy.services.NotificationService
import edu.bluejack20_2.chantuy.views.feedback.FeedbackActivity

class SettingsActivity : AppCompatActivity() {

    private val LARGE_KEY = "large"
    private val NOTIFICATION_KEY = "notification"
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var appSettingPreferences: SharedPreferences
    private lateinit var sharedPrefEdit: SharedPreferences.Editor
    var isLarge: Boolean = false
    var isNotificationOn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        appSettingPreferences = getSharedPreferences("AppSettingPreferences", 0)
        sharedPrefEdit = appSettingPreferences.edit()
        isLarge = appSettingPreferences.getBoolean(LARGE_KEY, false)
        isNotificationOn = appSettingPreferences.getBoolean(NOTIFICATION_KEY, false)

        Log.wtf("hehe", isLarge.toString())
        Log.wtf("hehe", isNotificationOn.toString())

//        when (isLarge) {
//            true -> {
//                var themeID: Int = R.style.Theme_Chantuy_FontLarge
//                setTheme(themeID)
//                Log.wtf("testis", "hehehe")
//            }
//            false -> {
//                var themeID: Int = R.style.Theme_Chantuy
//                setTheme(themeID)
//                Log.wtf("testis", "else")
//            }
//        }


        super.onCreate(savedInstanceState)
        setSwitchState()
        setSwitchListener()
        setViewFeedbackListener()
        setSendFeedbackListener()
    }

    private fun setViewFeedbackListener() {
        binding.viewFeedbackBtn.setOnClickListener {
            val intent = Intent(this, FeedbackActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setSendFeedbackListener() {
        binding.sendFeedbackBtn.setOnClickListener {
            val intent = Intent(this, InsertFeedbackActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setSwitchState() {
        when(isLarge){
            true -> binding.enableFontsizeSwitch.isChecked = true
            false -> binding.enableFontsizeSwitch.isChecked = false
        }

        when(isNotificationOn) {
            true -> binding.enableNotificationSwitch.isChecked = true
            false -> binding.enableNotificationSwitch.isChecked = false
        }
    }

    private fun setSwitchListener() {
        binding.enableNotificationSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                setNotification()
            } else {

            }
            sharedPrefEdit.putBoolean(NOTIFICATION_KEY, isChecked)
            sharedPrefEdit.commit()
            recreate()
        }

        binding.enableFontsizeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            sharedPrefEdit.putBoolean(LARGE_KEY, isChecked)
            sharedPrefEdit.commit()
            recreate()
        }
    }

    private fun setNotification() {
        val intent = Intent(this, NotificationService::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val timeAtNotificationOn = System.currentTimeMillis()
        val tenSecondsInMillis = 1000 * 10

        alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtNotificationOn + tenSecondsInMillis, pendingIntent)
        Log.i("SettingsActivity", "Notification Set!")
    }
}