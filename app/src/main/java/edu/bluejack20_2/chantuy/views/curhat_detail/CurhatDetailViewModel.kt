package edu.bluejack20_2.chantuy.views.curhat_detail

import android.content.Intent
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.models.CurhatComment
import edu.bluejack20_2.chantuy.repositories.CurhatCommentRepository
import edu.bluejack20_2.chantuy.repositories.CurhatRepository
import edu.bluejack20_2.chantuy.repositories.UserRepository

class CurhatDetailViewModel: ViewModel() {
    private var allCurhats: List<CurhatComment> = listOf()
    private var _comments: MutableLiveData<List<CurhatComment>> = MutableLiveData<List<CurhatComment>>().apply {
        value = listOf()
    }
    val comments: LiveData<List<CurhatComment>> get() = _comments

    private var _isFetchingData: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = true }
    val isFetchingData: LiveData<Boolean> get() = _isFetchingData

    var curhat: Curhat = Curhat()
    private var id = ""

    private var fromIndex = 0
    private var toIndex = 5

    fun getCurhatDetail(intent: Intent?) {
        _isFetchingData.value = true
        if (intent != null) {
            id = getCurhatId(intent)
        }

        CurhatRepository.getById(id) {
            curhat = it
            CurhatCommentRepository.getCommentsByCurhatId(id) { comments ->
                if (comments != null) {
                    allCurhats = comments
                    val toBeSliced = comments
                    if (comments.isNotEmpty() && comments.size >= 5) {
                        _comments.value = toBeSliced.subList(fromIndex, toIndex)
                    } else {
                        _comments.value = comments
                    }
                } else {
                    _comments.value = listOf()
                }
                _isFetchingData.value = false
            }
        }
    }

    private fun getCurhatId(intent: Intent) : String {
        val bundle = intent.extras
        val id = bundle!!.getString("id")
        return id!!
    }

    fun addComment(content: TextView) {
        val currentUserId = UserRepository.getCurrentUserId()
        CurhatCommentRepository.addComment(id, currentUserId, content.text.toString()) {
            content.text = ""
            if ((toIndex + 1) % 5 != 1) {
                toIndex += 1
            }
            Toast.makeText(content.context, "Comment succesfully added!", Toast.LENGTH_SHORT).show()
            getCurhatDetail(null)
        }
    }

    fun showMoreComments() {
        fromIndex += 5
        toIndex = getToIndex(toIndex)

        _comments.value = allCurhats.subList(fromIndex, toIndex)
    }

    private fun getToIndex(idx: Int): Int {
        if ((idx + 5) >= allCurhats.size) {
            return allCurhats.size
        }
        return idx + 5
    }

    fun shouldShowMore(): Boolean {
        return toIndex != allCurhats.size && _comments.value!!.isNotEmpty() && _comments.value!!.size >= 5
    }
}

