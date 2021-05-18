package edu.bluejack20_2.chantuy.views.settings

import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.bluejack20_2.chantuy.InsertFeedbackActivity
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.databinding.ActivitySettingsBinding
import edu.bluejack20_2.chantuy.models.GLOBALS
import edu.bluejack20_2.chantuy.repositories.CurhatCommentRepository
import edu.bluejack20_2.chantuy.repositories.CurhatRepository
import edu.bluejack20_2.chantuy.repositories.UserRepository
import edu.bluejack20_2.chantuy.services.NotificationService
import edu.bluejack20_2.chantuy.views.feedback.FeedbackActivity
import edu.bluejack20_2.chantuy.views.login.LoginActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var appSettingPreferences: SharedPreferences
    private lateinit var sharedPrefEdit: SharedPreferences.Editor
    var isLarge: Boolean = false
    var isNotificationOn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        appSettingPreferences = getSharedPreferences(GLOBALS.SETTINGS_PREFERENCES_NAME, 0)
        sharedPrefEdit = appSettingPreferences.edit()
        isLarge = appSettingPreferences.getBoolean(GLOBALS.SETTINGS_LARGE_KEY, false)
        isNotificationOn = appSettingPreferences.getBoolean(GLOBALS.SETTINGS_NOTIFICATION_KEY, false)

//        delete_account_btn

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
        setDeleteAccountListener()

        UserRepository.getCurrentUser {

            if (it?.isAdmin!!) {
                binding.viewFeedbackBtn.visibility = View.VISIBLE
                binding.sendFeedbackBtn.visibility = View.GONE
            } else {
                binding.viewFeedbackBtn.visibility = View.GONE
                binding.sendFeedbackBtn.visibility = View.VISIBLE
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
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

    private fun setDeleteAccountListener() {
        binding.deleteAccountBtn.setOnClickListener {

            AlertDialog.Builder(this).setMessage(
                 getString(R.string.confirm_delete_account)).setPositiveButton(android.R.string.yes
            ) { _, _ ->
                // Delete all

                val user= Firebase.auth.currentUser!!

                user.delete().addOnCompleteListener {task ->
                    if(task.isSuccessful){

                        GLOBALS.CHECK_USER=false

                        UserRepository.getUserById(user.uid).delete()

                        CurhatRepository.deleteUser(user.uid)

                        CurhatCommentRepository.deleteUser(user.uid)

                        Toast.makeText(this, getString(R.string.success_dela), Toast.LENGTH_SHORT).show()


                        intent= Intent(applicationContext, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                        startActivity(intent)

                        finish()

                    }else{
                        Toast.makeText(this, getString(R.string.fail_dela), Toast.LENGTH_SHORT).show()

                    }
                }
            }.setNegativeButton(android.R.string.no, null).show()

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
            sharedPrefEdit.putBoolean(GLOBALS.SETTINGS_NOTIFICATION_KEY, isChecked)
            sharedPrefEdit.commit()
            recreate()
        }

        binding.enableFontsizeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            sharedPrefEdit.putBoolean(GLOBALS.SETTINGS_LARGE_KEY, isChecked)
            sharedPrefEdit.commit()
            recreate()
        }
    }

    private fun cancelAlarm() {
        val intent = Intent(this, NotificationService::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(pendingIntent)

    }
}