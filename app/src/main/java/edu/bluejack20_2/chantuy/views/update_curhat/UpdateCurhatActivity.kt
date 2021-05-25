package edu.bluejack20_2.chantuy.views.update_curhat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.lifecycle.Observer
import edu.bluejack20_2.chantuy.MainActivity
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.models.GLOBALS

class UpdateCurhatActivity : AppCompatActivity() {

    private lateinit var appSettingPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        appSettingPreferences = getSharedPreferences(GLOBALS.SETTINGS_PREFERENCES_NAME, Context.MODE_PRIVATE)
        editor = appSettingPreferences.edit()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_curhat)

        val curhatIdTv: TextView = findViewById(R.id.update_curhat_id_edit)
        val contentTv: EditText = findViewById(R.id.update_curhat_content_edit_text)
        val topicTv: AutoCompleteTextView = findViewById(R.id.update_topic_auto_complete)
        val anonymousCb: CheckBox = findViewById(R.id.update_set_anonymous_checkbox)
        val currentTopicTv: TextView = findViewById(R.id.update_current_curhat_topic)
        val updateBtn: Button = findViewById(R.id.insert_feedback_btn)

        val viewModel = UpdateCurhatViewModel()
        viewModel.setTopicAutocomplete(topicTv)
        viewModel.getCurhat(intent)

        viewModel.curhat.observe(this, Observer {
            curhatIdTv.text = it.id
            contentTv.setText(it.content)
            currentTopicTv.text =
                "${currentTopicTv.context.getString(R.string.current)}: ${viewModel.currentTopicName} (${currentTopicTv.context.getString(R.string.default_topic)})"
            anonymousCb.isChecked = it.isAnonymous
        })

        updateBtn.setOnClickListener {
            if (contentTv.text.isEmpty()) {
              contentTv.error = getString(R.string.content_empty_error)
            }  else {
                viewModel.onUpdate(
                    contentTv,
                    topicTv.text.toString(),
                    anonymousCb.isChecked
                ) {
                    Toast.makeText(this, getString(R.string.toast_update_succesfully), Toast.LENGTH_SHORT).show()
                    editor.putBoolean(GLOBALS.CURHAT_UPDATED_KEY, true)
                    editor.commit()
                    Log.i("TESTTT", "UpdateCurhat: " + appSettingPreferences.getBoolean(GLOBALS.CURHAT_UPDATED_KEY, false).toString())
                    finish()
                }
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
}