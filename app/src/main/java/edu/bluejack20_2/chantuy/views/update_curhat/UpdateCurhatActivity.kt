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
                "current: ${viewModel.currentTopicName} (leave the field blank to use current topic)"
            anonymousCb.isChecked = it.isAnonymous
        })

        updateBtn.setOnClickListener {
            if (contentTv.text.isEmpty()) {
              contentTv.error = "Content must not be empty"
            }  else {
                viewModel.onUpdate(
                    contentTv,
                    topicTv.text.toString(),
                    anonymousCb.isChecked
                ) {
                    Toast.makeText(this, "Curhat Updated Succesfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}