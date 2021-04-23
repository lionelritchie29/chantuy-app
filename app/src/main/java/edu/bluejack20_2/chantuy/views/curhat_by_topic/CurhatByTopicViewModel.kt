package edu.bluejack20_2.chantuy.views.curhat_by_topic

import android.app.Application
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.models.CurhatTopic
import edu.bluejack20_2.chantuy.repositories.CurhatRepository
import edu.bluejack20_2.chantuy.repositories.CurhatTopicRepository

class CurhatByTopicViewModel
    (application: Application) : AndroidViewModel(application) {
    private var _shuffledTopics: MutableLiveData<List<CurhatTopic>> =
        MutableLiveData<List<CurhatTopic>>().apply {
            value = listOf()
        }
    val topics: LiveData<List<CurhatTopic>> get() = _shuffledTopics

    private val topicsString = mutableListOf<String>()
    private var _topics = listOf<CurhatTopic>()

    private var _filteredCurhats: MutableLiveData<List<Curhat>> =
        MutableLiveData<List<Curhat>>().apply {  value = listOf() }
    val filteredCurhats: LiveData<List<Curhat>> get() = _filteredCurhats

    init {
        CurhatTopicRepository.getAll { topics ->
            _shuffledTopics.value = topics.shuffled().take(3)
            _topics = topics
            for (topic in topics) {
                topicsString.add(topic.name)
            }
        }
    }

    fun setTopicAutocomplete(view: AutoCompleteTextView) {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(getApplication(), android.R.layout.select_dialog_item, topicsString)
        view.threshold = 1
        view.setAdapter(adapter)
    }

    fun OnFilter(topicName: String) {
        val index = topicsString.indexOf(topicName)
        if (index != -1) {
            CurhatRepository.getByTopic(_topics.get(index).id) { curhats ->
                _filteredCurhats.value = curhats
                for (curhat in _filteredCurhats.value!!) {
                    Log.i("CurhatByTopicViewModel", curhat.content)
                }
                Log.i("CurhatByTopicViewModel", _filteredCurhats.value!!.size.toString())
            }
        }
    }
}