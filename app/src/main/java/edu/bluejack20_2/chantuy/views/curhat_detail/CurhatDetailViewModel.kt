package edu.bluejack20_2.chantuy.views.curhat_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.models.CurhatComment

class CurhatDetailViewModel: ViewModel() {

    val dummyComment: List<CurhatComment> = listOf(
        CurhatComment("1", "Test Comment 1"),
        CurhatComment("2", "Test Comment 2"),
        CurhatComment("3", "Test Comment 3")
    )

    private var _comments: MutableLiveData<List<CurhatComment>> = MutableLiveData<List<CurhatComment>>().apply {
        value = dummyComment
    }

    val comments: LiveData<List<CurhatComment>> get() = _comments
}