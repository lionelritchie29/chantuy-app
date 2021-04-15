import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack20_2.chantuy.models.Curhat

class HottestCurhatViewModel : ViewModel(){
    val initCurhats : List<Curhat> = listOf(
        Curhat("1", "Test1", 1, 2, 3, null, null, null),
        Curhat("2", "Test2", 1, 2, 3, null, null, null),
        Curhat("3", "Test3", 1, 2, 3, null, null, null)
    )

    private var _curhats: MutableLiveData<List<Curhat>> = MutableLiveData<List<Curhat>>().apply {
        postValue(initCurhats)
    }
    val curhats: LiveData<List<Curhat>> get() = _curhats

}