package edu.bluejack20_2.chantuy.views.feedback

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack20_2.chantuy.models.Feedback
import edu.bluejack20_2.chantuy.repositories.FeedbackRepository

class FeedbackViewModel: ViewModel() {
    private var _feedbacks = MutableLiveData<List<Feedback>>().apply {
        value = listOf()
    }
    val feedbacks: LiveData<List<Feedback>> get() = _feedbacks

    init {
        FeedbackRepository.getAll {
            _feedbacks.value = it
        }
    }
}