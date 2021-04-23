package edu.bluejack20_2.chantuy.views.search_curhat

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.models.Curhat
import edu.bluejack20_2.chantuy.repositories.CurhatRepository

class SearchCurhatFragment : Fragment() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.i("Testing","aaaaaaaaa")

        CurhatRepository.getCurhatBySearch("test",0).get().addOnSuccessListener {docs->
            val results= docs.toObjects(Curhat::class.java)
            Log.i("Test","Sampe sih sebenrnya")
            for (result in results){
                Log.i("Testing","result content : "+result.content)
            }
        }


        return inflater.inflate(R.layout.fragment_search_curhat, container, false)
    }
}