package edu.bluejack20_2.chantuy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.bluejack20_2.chantuy.views.`curhat-by-topic`.CurhatByTopicFragment
import edu.bluejack20_2.chantuy.views.`hottest-curhat`.HottestCurhatFragment
import edu.bluejack20_2.chantuy.views.`newest-curhat`.NewestCurhatFragment
import edu.bluejack20_2.chantuy.views.`search-curhat`.SearchCurhatFragment
import edu.bluejack20_2.chantuy.views.user_profile.UserProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigation: BottomNavigationView
    private val PAGES_COUNT = 5
    private val menuIdList : List<Int> = listOf(
        R.id.hottest_menu_item,
        R.id.newest_menu_item,
        R.id.topic_menu_item,
        R.id.search_menu_item,
        R.id.profile_menu_item
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        disableNightMode();

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setViewPager()
        setBottomMenuItemListener()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.action_bar_items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_menu_item -> {
                Log.i("MainActivity", "Settings clicked!")
            }
            R.id.notification_menu_item -> {
                Log.i("MainActivity", "Notifications clicked!")
            }
        }
        return super.onOptionsItemSelected(item)
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
        val menuOnItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {item ->
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

    private inner class CurhatViewSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

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