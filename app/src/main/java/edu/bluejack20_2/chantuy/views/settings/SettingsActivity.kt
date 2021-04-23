package edu.bluejack20_2.chantuy.views.settings

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private val LARGE_KEY = "large"
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var appSettingPreferences: SharedPreferences
    private lateinit var sharedPrefEdit: SharedPreferences.Editor
    var isLarge: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        appSettingPreferences = getSharedPreferences("AppSettingPreferences", 0)
        sharedPrefEdit = appSettingPreferences.edit()
        isLarge = appSettingPreferences.getBoolean(LARGE_KEY, false)

        Log.wtf("hehe", isLarge.toString())

        when (isLarge) {
            true -> {
                var themeID: Int = R.style.Theme_Chantuy_FontLarge
                setTheme(themeID)
                Log.wtf("testis", "hehehe")
            }
            false -> {
                var themeID: Int = R.style.Theme_Chantuy
                setTheme(themeID)
                Log.wtf("testis", "else")
            }
        }


        super.onCreate(savedInstanceState)
        setSwitchState()
        setFontSpinnerAdapter()
        setSwitchListener()
    }

    private fun setSwitchState() {
        when(isLarge){
            true -> binding.enableNotificationSwitch.isChecked = true
            false -> binding.enableNotificationSwitch.isChecked = false
        }
    }

    private fun setSwitchListener() {
        binding.enableNotificationSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            sharedPrefEdit.putBoolean(LARGE_KEY, isChecked)
            sharedPrefEdit.commit()
            recreate()
        }
    }

    private fun setFontSpinnerAdapter() {
        ArrayAdapter.createFromResource(
            this,
            R.array.font_options,
            android.R.layout.simple_spinner_item
        ).also {adapter  ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.fontSpinner.adapter = adapter
        }
    }
}