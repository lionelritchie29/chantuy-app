package edu.bluejack20_2.chantuy.views.curhat_detail

import android.content.Intent
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.models.CurhatComment
import edu.bluejack20_2.chantuy.repositories.CurhatCommentRepository
import edu.bluejack20_2.chantuy.repositories.CurhatRepository

class CurhatDetailViewModel: ViewModel() {
    private var _comments: MutableLiveData<List<CurhatComment>> = MutableLiveData<List<CurhatComment>>().apply {
        value = listOf()
    }
    val comments: LiveData<List<CurhatComment>> get() = _comments
    var curhat: Curhat = Curhat()
    private var id = ""

    fun getCurhatDetail(intent: Intent?) {
        if (intent != null) {
            id = getCurhatId(intent)
        }

        CurhatRepository.getById(id) {
            curhat = it
            CurhatCommentRepository.getCommentsById(id) { comments ->
                if (comments != null) {
                    _comments.value = comments
                } else {
                    _comments.value = listOf()
                }
            }
        }
    }

    private fun getCurhatId(intent: Intent) : String {
        val bundle = intent.extras
        val id = bundle!!.getString("id")
        return id!!
    }

    fun addComment(content: TextView) {
        CurhatCommentRepository.addComment(id, "", content.text.toString()) {
            getCurhatDetail(null)
            content.text = ""
        }
    }


}