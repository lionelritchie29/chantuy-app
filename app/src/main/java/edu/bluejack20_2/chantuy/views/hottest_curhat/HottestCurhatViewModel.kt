import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack20_2.chantuy.models.Curhat
import java.time.LocalDateTime

class HottestCurhatViewModel : ViewModel(){
    val initCurhats : List<Curhat> = listOf(
        Curhat("1", "Test1", 1, 2, 3, "", ""),
        Curhat("2", "Test2", 1, 2, 3, "", ""),
        Curhat("3", "Test3", 1, 2, 3, "", ""),
        Curhat("4", "Test4", 1, 2, 3, "", ""),
        Curhat("5", "Test5", 1, 2, 3, "", ""),
        Curhat("6", "Test6", 1, 2, 3, "", ""),
        Curhat("7", "Test7", 1, 2, 3, "", ""),
        Curhat("8", "Test8", 1, 2, 3, "", ""),
        Curhat("9", "Test9", 1, 2, 3, "", ""),
        Curhat("10", "Test10", 1, 2, 3, "", ""),
        Curhat("11", "Test11", 1, 2, 3, "", ""),
        Curhat("12", "Test12", 1, 2, 3, "", ""),
        Curhat("13", "Test13", 1, 2, 3, "", ""),
        Curhat("14", "Test14", 1, 2, 3, "", ""),
        Curhat("15", "Test15", 1, 2, 3, "", "")
    )

    private var _curhats: MutableLiveData<List<Curhat>> = MutableLiveData<List<Curhat>>().apply {
        postValue(initCurhats)
    }
    val curhats: LiveData<List<Curhat>> get() = _curhats

}