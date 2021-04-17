package edu.bluejack20_2.chantuy.views.insert_curhat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.models.CurhatTopic
import edu.bluejack20_2.chantuy.repositories.CurhatTopicRepository

class InsertCurhatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_curhat)

        setTopicAutocomplete()
    }

    private fun setTopicAutocomplete() {
        var topics: List<CurhatTopic>
        val topicsString = mutableListOf<String>()
        
        CurhatTopicRepository.getAll { topicsFromRepo ->
            topics = topicsFromRepo
            for (topic in topics) {
                topicsString.add(topic.name)
            }
        }

        val topicAutoCompleteView: AutoCompleteTextView = findViewById(R.id.topic_auto_complete)
        val adapter: ArrayAdapter<String>
                = ArrayAdapter<String>(this, android.R.layout.select_dialog_item, topicsString)
        topicAutoCompleteView.threshold = 1
        topicAutoCompleteView.setAdapter(adapter)
    }
}