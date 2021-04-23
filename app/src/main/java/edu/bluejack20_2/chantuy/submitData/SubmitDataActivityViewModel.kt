package edu.bluejack20_2.chantuy.submitData

import android.widget.TextView
import com.google.firebase.Timestamp
import edu.bluejack20_2.chantuy.repositories.UserRepository

class SubmitDataActivityViewModel {
    var genderString: String ="Male"
    var dob: Timestamp = Timestamp.now()
    var imageUrl: String ="https://firebasestorage.googleapis.com/v0/b/chantuy-app.appspot.com/o/imageUploads%2Fdefault_male.png?alt=media&token=9ef4eebb-ba41-4bb3-9f90-fb33218cffe1"
    fun post(textView: TextView){
        UserRepository
    }
}