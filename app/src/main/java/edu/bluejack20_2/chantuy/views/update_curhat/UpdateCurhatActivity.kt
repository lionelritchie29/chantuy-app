package edu.bluejack20_2.chantuy.views.update_curhat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.lifecycle.Observer
import edu.bluejack20_2.chantuy.R

class UpdateCurhatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_curhat)

        val curhatIdTv: TextView = findViewById(R.id.update_curhat_id_edit)
        val contentTv: TextView = findViewById(R.id.edit_text_update_curhat_content)
        val topicTv: AutoCompleteTextView = findViewById(R.id.update_topic_auto_complete)
        val anonymousCb: CheckBox = findViewById(R.id.update_set_anonymous_checkbox)
        val currentTopicTv: TextView = findViewById(R.id.update_current_curhat_topic)
        val updateBtn: Button = findViewById(R.id.update_update__curhat_btn)

        val viewModel = UpdateCurhatViewModel()
        viewModel.setTopicAutocomplete(topicTv)
        viewModel.getCurhat(intent)

        viewModel.curhat.observe(this, Observer {
            curhatIdTv.text = it.id
            contentTv.text = it.content
            currentTopicTv.text =
                "current: ${viewModel.currentTopicName} (leave the field blank to use current topic)"
            anonymousCb.isChecked = it.isAnonymous
        })

        updateBtn.setOnClickListener {
            viewModel.onUpdate(
                contentTv.text.toString(),
                topicTv.text.toString(),
                anonymousCb.isChecked
            ) {
                finish()
            }
        }
    }
}