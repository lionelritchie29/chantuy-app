import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.repositories.CurhatRepository
import java.time.LocalDateTime

class HottestCurhatViewModel : ViewModel(){
    val initCurhats : List<Curhat> = listOf()

    private var _curhats: MutableLiveData<List<Curhat>> = MutableLiveData<List<Curhat>>().apply {
        postValue(initCurhats)
    }
    val curhats: LiveData<List<Curhat>> get() = _curhats

    init {
//        CurhatRepository.addDummy()
        CurhatRepository.getAll { curhats ->
            _curhats.value = curhats
        }
    }

}