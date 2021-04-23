package edu.bluejack20_2.chantuy.views.update_curhat

import android.content.Intent
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.models.CurhatTopic
import edu.bluejack20_2.chantuy.repositories.CurhatRepository
import edu.bluejack20_2.chantuy.repositories.CurhatTopicRepository

class UpdateCurhatViewModel : ViewModel() {
    private var _curhat: MutableLiveData<Curhat> =
        MutableLiveData<Curhat>().apply { value = Curhat() }
    val curhat: LiveData<Curhat> get() = _curhat
    private lateinit var curhatId: String

    private lateinit var topics: List<CurhatTopic>
    private val topicsString = mutableListOf<String>()

    var currentTopicName: String = ""

    init {
        CurhatTopicRepository.getAll { topicsFromRepo ->
            topics = topicsFromRepo
            for (topic in topics) {
                topicsString.add(topic.name)
            }
        }
    }

    fun getCurhat(intent: Intent) {
        curhatId = getCurhatIdFromBundle(intent)

        CurhatRepository.getById(curhatId) {
            val retrievedCurhat = it
            CurhatTopicRepository.get(it.topic) {
                currentTopicName = it.name
                _curhat.value = retrievedCurhat
            }
        }
    }

    fun getCurhatIdFromBundle(intent: Intent): String {
        val bundle = intent.extras
        val id = bundle!!.getString("curhatId")
        return id!!
    }

    fun setTopicAutocomplete(topicTv: AutoCompleteTextView) {
        val adapter: ArrayAdapter<String>
                = ArrayAdapter(topicTv.context, android.R.layout.select_dialog_item, topicsString)
        topicTv.threshold = 1
        topicTv.setAdapter(adapter)
    }

    fun onUpdate(content: EditText, topic: String, isAnon: Boolean, callback: () -> Unit) {
        if (content.text.isEmpty()) {
            content.error = "Content must not be empty"
            return
        }

        getTopicId(topic) { newTopicId ->
            val curhat = hashMapOf(
                "topic" to newTopicId,
                "content" to content.text.toString(),
                "anonymous" to isAnon
            )
            CurhatRepository.update(curhatId, curhat) {
                callback()
            }
        }
    }

    fun getTopicId(topicName: String, callback: (String) -> Unit) {
        if (topicName.length <= 0) {
            val index = topicsString.indexOf(currentTopicName)
            callback(topics.get(index).id)
        } else {
            val index = topicsString.indexOf(topicName)
            if (index == -1) {
                CurhatTopicRepository.addTopic(topicName) {topicId ->
                    callback(topicId)
                }
            } else {
                callback(topics.get(index).id)
            }
        }
    }
}