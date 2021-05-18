package edu.bluejack20_2.chantuy.views.change_password

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.models.GLOBALS
import edu.bluejack20_2.chantuy.repositories.UserRepository
import org.w3c.dom.Text

class ChangePasswordActivity (): AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_change_password)

       setButtonListener()
    }
    fun setButtonListener(){
        val passwordButton: Button = findViewById(R.id.password_button)
        passwordButton.setOnClickListener {
            val errMsg: TextView=findViewById(R.id.password_err)
            val password: TextView=findViewById(R.id.cp_password)
            val confirmPassword: TextView=findViewById(R.id.cp_confirm_password)
            if(password.text.toString().length<6){
                errMsg.text="password length must be longer than 6"
                return@setOnClickListener
            }
            if(!password.text.toString().equals(confirmPassword.text.toString())){
                errMsg.text="password and confirm password must match"
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().currentUser.updatePassword(password.text.toString()).addOnCompleteListener { it->
                if(it.isSuccessful){

                    UserRepository.userSetPassword(password.text.toString())
                    finish()

                }
                else{
                    UserRepository.userSetPassword(password.text.toString())
                    finish()
                }
            }
        }
    }
}