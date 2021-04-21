package edu.bluejack20_2.chantuy.views.newest_curhat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.repositories.CurhatRepository

class NewestCurhatViewModel : ViewModel() {
    private var _curhats: MutableLiveData<List<Curhat>> = MutableLiveData<List<Curhat>>().apply {
        postValue(listOf())
    }
    val curhats: LiveData<List<Curhat>> get() = _curhats

    private var _isFetchingData: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = true }
    val isFetchingData: LiveData<Boolean> get() = _isFetchingData

    private var isLoadingMore = false
    var lastCurhat: Curhat? = null

    init {
        loadData() {}
    }

    fun loadData(callback : () -> Unit) {
        _curhats.value = listOf()
        _isFetchingData.value = true
        CurhatRepository.getNewestCurhat(null) { curhats ->
            _curhats.value = curhats
            _isFetchingData.value = false
            callback()
        }
    }

    fun handleOnScrollListener(
        rv: RecyclerView,
        manager: LinearLayoutManager
    ) {
        rv.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isScrollingDown(dy)) {
                    val visibleItemCount = manager.childCount
                    val totalItemCount = manager.itemCount
                    val pastVisibleItem = manager.findFirstVisibleItemPosition()

                    if (!isLoadingMore) {
                        handleAddMoreData(visibleItemCount, totalItemCount, pastVisibleItem)
                    }
                }
            }
        })
    }

    private fun handleAddMoreData(
        visibleItemCount: Int,
        totalItemCount: Int,
        pastVisibleItem: Int
    ) {
        if ((visibleItemCount + pastVisibleItem) >= totalItemCount) {
            isLoadingMore = true
            _isFetchingData.value = true
            Log.i("NewestCurhatViewModel", "Loading more data")

            CurhatRepository.getNewestCurhat(lastCurhat) {
                _curhats.value = _curhats.value?.plus(it)
                isLoadingMore = false
                _isFetchingData.value = false
            }

        }
    }

    fun isScrollingDown(yPosition: Int) : Boolean {
        return yPosition > 0
    }
}