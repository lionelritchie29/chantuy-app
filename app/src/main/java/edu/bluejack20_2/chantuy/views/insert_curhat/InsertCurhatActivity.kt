package edu.bluejack20_2.chantuy.views.insert_curhat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import edu.bluejack20_2.chantuy.MainActivity
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.models.CurhatTopic
import edu.bluejack20_2.chantuy.repositories.CurhatRepository
import edu.bluejack20_2.chantuy.repositories.CurhatTopicRepository

class InsertCurhatActivity : AppCompatActivity() {
    private lateinit var topicAutoCompleteView: AutoCompleteTextView
    private lateinit var topics: List<CurhatTopic>
    private val topicsString = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_curhat)

        setTopicAutocomplete()
        onAddCurhat()
    }

    init {
        CurhatTopicRepository.getAll { topicsFromRepo ->
            topics = topicsFromRepo
            for (topic in topics) {
                topicsString.add(topic.name)
            }
        }
    }

    private fun setTopicAutocomplete() {
        topicAutoCompleteView = findViewById(R.id.topic_auto_complete)
        val adapter: ArrayAdapter<String>
                = ArrayAdapter<String>(this, android.R.layout.select_dialog_item, topicsString)
        topicAutoCompleteView.threshold = 1
        topicAutoCompleteView.setAdapter(adapter)
    }

    private fun onAddCurhat() {
        val addBtn: Button = findViewById(R.id.insert_add_curhat_btn)
        val content: TextView = findViewById(R.id.edit_text_curhat_content)

        addBtn.setOnClickListener {
            val topicIndex = topicsString.indexOf(topicAutoCompleteView.text.toString())
            if (topicIndex == -1) {
                val newTopic = topicAutoCompleteView.text.toString()
                CurhatTopicRepository.addTopic(newTopic, callback = {newTopicId ->
                    CurhatRepository.addCurhat(content.text.toString(), newTopicId) {
                        Toast.makeText(this, "Insert Success", Toast.LENGTH_SHORT)
                        moveToMainActivity()

                    }
                })
            } else {
                CurhatRepository.addCurhat(content.text.toString(), topics.get(topicIndex).id) {
                    Toast.makeText(this, "Insert Success", Toast.LENGTH_SHORT)
                    moveToMainActivity()
                }
            }
        }
    }

    private fun moveToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}