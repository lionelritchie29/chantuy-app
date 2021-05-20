package edu.bluejack20_2.chantuy.views.search_curhat

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.repositories.CurhatRepository

class SearchCurhatViewModel {
    var searchType: Int=1
    var searchString: String=""
    private var _curhats: MutableLiveData<List<Curhat>> = MutableLiveData<List<Curhat>>().apply {
        postValue(listOf())
    }
    var curhats:   MutableLiveData<List<Curhat>> = _curhats


    @RequiresApi(Build.VERSION_CODES.O)

    fun search(){

        chooseQuery().addOnSuccessListener { docs->
            val results= docs.toObjects(Curhat::class.java)

            val tempResult= mutableListOf<Curhat>()
            for (result in results){
                if(result.content.toLowerCase().contains(searchString.toLowerCase())){
                    tempResult?.add(result)
                }
            }
            curhats.value=tempResult.toList()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun chooseQuery(): Task<QuerySnapshot>{
        when(searchType){
            1-> return CurhatRepository.getCurhatBySearch(searchString,true).get()
            2-> return CurhatRepository.getCurhatBySearch(searchString,false).get()
            else-> return CurhatRepository.getCurhatBySearch(searchString,searchType-3).get()
        }

    }
}