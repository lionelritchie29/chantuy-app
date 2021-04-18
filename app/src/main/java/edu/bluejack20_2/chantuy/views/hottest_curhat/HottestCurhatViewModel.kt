import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.repositories.CurhatRepository

class HottestCurhatViewModel : ViewModel(){
    private var _curhats: MutableLiveData<List<Curhat>> = MutableLiveData<List<Curhat>>().apply {
        postValue(listOf())
    }
    val curhats: LiveData<List<Curhat>> get() = _curhats
    var isLoadingMore = false
    var lastCurhat: Curhat? = null

    init {
//        CurhatRepository.addDummy()
        CurhatRepository.getHottestCurhat (null) { curhats ->
            _curhats.value = curhats
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
            Log.i("HottestCurhatViewModel", "Loading more data")

            CurhatRepository.getHottestCurhat(lastCurhat) {
                _curhats.value = _curhats.value?.plus(it)
                isLoadingMore = false
            }

        }
    }

    fun isScrollingDown(yPosition: Int) : Boolean {
        return yPosition > 0
    }

}