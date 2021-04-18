package edu.bluejack20_2.chantuy.views.newest_curhat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.repositories.CurhatRepository

class NewestCurhatViewModel : ViewModel() {
    private var _curhats: MutableLiveData<List<Curhat>> = MutableLiveData<List<Curhat>>().apply {
        postValue(listOf())
    }
    val curhats: LiveData<List<Curhat>> get() = _curhats

    init {
//        CurhatRepository.addDummy()
        CurhatRepository.getNewestCurhat { curhats ->
            _curhats.value = curhats
            Log.i("NewestCurhatViewModel", curhats.toString())
        }
    }
}