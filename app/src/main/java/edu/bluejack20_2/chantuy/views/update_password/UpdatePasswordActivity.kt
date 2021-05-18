package edu.bluejack20_2.chantuy.views.update_password

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import android.view.MenuItem
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.repositories.UserRepository
import org.w3c.dom.Text

class UpdatePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)

        setButtonListener()
    }
    
    fun setButtonListener(){
        val passwordButton: Button = findViewById(R.id.upassword_button)
        passwordButton.setOnClickListener {
            val errMsg: TextView =findViewById(R.id.upassword_err)
            val oldPassword: TextView=findViewById(R.id.up_old_pass)
            val password: TextView =findViewById(R.id.up_password)
            val confirmPassword: TextView =findViewById(R.id.up_confirm_password)
            if(password.text.toString().length<6){
                errMsg.text="password length must be longer than 6"
                return@setOnClickListener
            }
            else if(!password.text.toString().equals(confirmPassword.text.toString())){
                errMsg.text="password and confirm password must match"
                return@setOnClickListener
            }

            UserRepository.getUserById(FirebaseAuth.getInstance().currentUser.uid).get().addOnSuccessListener {
                Log.i("Testing"," a "+ it["password"])
                if(!it["password"]?.equals(oldPassword.text.toString())!!){
                    errMsg.text="Wrong old password"
                    return@addOnSuccessListener
                }

                    FirebaseAuth.getInstance().currentUser.updatePassword(password.text.toString()).addOnCompleteListener { it->
                        if(it.isSuccessful){
                            UserRepository.userSetPassword(password.text.toString())
                            finish()
                        }
                        else{
                            errMsg.text="An error has occured, try relogging in"
                        }
                    }

            }




        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}