package edu.bluejack20_2.chantuy.views.change_password

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.repositories.UserRepository


class ChangePasswordActivity (): AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_change_password)
        reAuth()
       setButtonListener()
    }
    fun reAuth(){

        val currUser=FirebaseAuth.getInstance().currentUser
        val googleAcc= GoogleSignIn.getLastSignedInAccount(this)
        if(googleAcc!=null){
            val credential =
                GoogleAuthProvider.getCredential(googleAcc.getIdToken(), null)
            currUser.reauthenticate(credential)
                .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                    if(task.isSuccessful){
                        Log.i("Testing","Sukses gan")
                    }
                    else{
                        Log.i("Testing","Gagal")
                    }
                })
        }
    }
    fun setButtonListener(){
        val passwordButton: Button = findViewById(R.id.password_button)
        passwordButton.setOnClickListener {
            val errMsg: TextView=findViewById(R.id.password_err)
            val password: TextView=findViewById(R.id.cp_password)
            val confirmPassword: TextView=findViewById(R.id.cp_confirm_password)
            if(password.text.toString().length<6){
                errMsg.text=getString(R.string.validate_pl)
                return@setOnClickListener
            }
            if(!password.text.toString().equals(confirmPassword.text.toString())){
                errMsg.text=getString(R.string.validate_pcp)
                return@setOnClickListener
            }




            FirebaseAuth.getInstance().currentUser.updatePassword(password.text.toString()).addOnCompleteListener { it->
                if(it.isSuccessful){
                    UserRepository.userSetPassword(password.text.toString())
                    finish()
                }
                else{


                    finish()
                }
            }
        }
    }
}