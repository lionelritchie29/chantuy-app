package edu.bluejack20_2.chantuy

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.view.menu.MenuBuilder
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import edu.bluejack20_2.chantuy.models.GLOBALS
import edu.bluejack20_2.chantuy.repositories.UserRepository
import edu.bluejack20_2.chantuy.services.NotificationService
import edu.bluejack20_2.chantuy.submitData.SubmitDataActivity
import edu.bluejack20_2.chantuy.views.change_password.ChangePasswordActivity
import edu.bluejack20_2.chantuy.views.curhat_by_topic.CurhatByTopicFragment
import edu.bluejack20_2.chantuy.views.hottest_curhat.HottestCurhatFragment
import edu.bluejack20_2.chantuy.views.insert_curhat.InsertCurhatActivity
import edu.bluejack20_2.chantuy.views.login.LoginActivity
import edu.bluejack20_2.chantuy.views.newest_curhat.NewestCurhatFragment
import edu.bluejack20_2.chantuy.views.notification.NotificationActivity
import edu.bluejack20_2.chantuy.views.search_curhat.SearchCurhatFragment
import edu.bluejack20_2.chantuy.views.settings.SettingsActivity
import edu.bluejack20_2.chantuy.views.user_profile.UserProfileFragment
import java.lang.Exception
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private lateinit var appSettingPreferences: SharedPreferences
    var isLarge: Boolean = false

    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigation: BottomNavigationView
    private val PAGES_COUNT = 5
    private val menuIdList: List<Int> = listOf(
        R.id.hottest_menu_item,
        R.id.newest_menu_item,
        R.id.topic_menu_item,
        R.id.search_menu_item,
        R.id.profile_menu_item
    )

    override fun onCreate(savedInstanceState: Bundle?) {

        GLOBALS.CHECK_USER=true

        disableNightMode()
        setFontSize()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()
        setViewPager()
        setViewPager()
        setBottomMenuItemListener()

        setBottomMenuItemListener()
        testPushNotification()
        setNotification()
        checkUser()

    }
    private fun checkUser(){

        if(UserRepository.getCurrentUserQ()?.uid==null)return
        if(!GLOBALS.CHECK_USER)return
        UserRepository.getUserById(UserRepository.getCurrentUserQ()?.uid!!)!!.addSnapshotListener{
            user,e ->
            if(user==null||!GLOBALS.CHECK_USER) return@addSnapshotListener;
            else if(user?.get("password")==null){
                val intent  = Intent(this, ChangePasswordActivity::class.java)
                startActivity(intent)
            }
            else if(user?.get("gender") ==null){
                val intent  = Intent(this, SubmitDataActivity::class.java)
                startActivity(intent)
            }


        }
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "ChantuyReminderChannel"
            val description = "Channel for Chantuy Reminder"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(GLOBALS.NOTIFICATION_CHANNEL_ID, name, importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java);
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setNotification() {
        val intent = Intent(this, NotificationService::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val timeAtNotificationOn = System.currentTimeMillis()
        val tenSecondsInMillis = 1000 * 10

        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + tenSecondsInMillis,
            AlarmManager.INTERVAL_HOUR,
            pendingIntent
        )
    }

    private fun setFontSize() {
        appSettingPreferences = getSharedPreferences(GLOBALS.SETTINGS_PREFERENCES_NAME, 0)
        isLarge = appSettingPreferences.getBoolean(GLOBALS.SETTINGS_LARGE_KEY, false)

        when (isLarge) {
            true -> {
                val themeID: Int = R.style.Theme_Chantuy_FontLarge
                setTheme(themeID)

            }
            else -> {
                val themeID: Int = R.style.Theme_Chantuy
                setTheme(themeID)
            }
        }
    }

    private fun testPushNotification() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.action_bar_items, menu)

        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_curhat_menu_item -> {
                moveToAddCurhatActivity()
            }
            R.id.settings_menu_item -> {
                moveToSettingsActivity()
            }
            R.id.notification_menu_item -> {
                moveToNotificationsActivity()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun moveToNotificationsActivity(){
        val intent=Intent(this, NotificationActivity::class.java)
        startActivity(intent)
    }
    private fun moveToSettingsActivity() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)

    }

    private fun moveToAddCurhatActivity() {
        val intent = Intent(this, InsertCurhatActivity::class.java)
        startActivity(intent)
    }

    private fun disableNightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun setViewPager() {
        val pagerAdapter = CurhatViewSlidePagerAdapter(this)
        viewPager = findViewById(R.id.curhatViewPager)
        viewPager.adapter = pagerAdapter

        val curhatPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottomNavigation.selectedItemId = menuIdList[position]
            }
        }

        viewPager.registerOnPageChangeCallback(curhatPageChangeCallback)
    }

    private fun setBottomMenuItemListener() {
        val menuOnItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.hottest_menu_item -> {
                        viewPager.currentItem = 0
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.newest_menu_item -> {
                        viewPager.currentItem = 1
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.topic_menu_item -> {
                        viewPager.currentItem = 2
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.search_menu_item -> {
                        viewPager.currentItem = 3
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.profile_menu_item -> {
                        viewPager.currentItem = 4
                        return@OnNavigationItemSelectedListener true
                    }
                    else -> false
                }
            }

        bottomNavigation = findViewById(R.id.navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(menuOnItemSelectedListener)
    }

    private inner class CurhatViewSlidePagerAdapter(fa: FragmentActivity) :
        FragmentStateAdapter(fa) {

        override fun getItemCount(): Int = PAGES_COUNT

        override fun createFragment(position: Int): Fragment {
            lateinit var currentFragment: Fragment
            when (position) {
                0 -> currentFragment = HottestCurhatFragment()
                1 -> currentFragment = NewestCurhatFragment()
                2 -> currentFragment = CurhatByTopicFragment()
                3 -> currentFragment = SearchCurhatFragment()
                4 -> currentFragment = UserProfileFragment()
            }
            return currentFragment
        }


    }
}