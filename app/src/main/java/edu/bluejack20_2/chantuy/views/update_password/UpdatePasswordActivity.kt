package edu.bluejack20_2.chantuy.views.update_password

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import android.view.MenuItem
import android.widget.Toast
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.repositories.UserRepository
import edu.bluejack20_2.chantuy.utils.AuthUtil
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
            val oldPassword: TextView=findViewById(R.id.up_old_pass)
            val password: TextView =findViewById(R.id.up_password)
            val confirmPassword: TextView =findViewById(R.id.up_confirm_password)
            if(password.text.toString().length<6){
                Toast.makeText(this, getString(R.string.validate_pl) , Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if(!password.text.toString().equals(confirmPassword.text.toString())){
                Toast.makeText(this, getString(R.string.validate_pcp) , Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            UserRepository.getUserById(FirebaseAuth.getInstance().currentUser.uid).get().addOnSuccessListener {

                if(!it["password"]?.equals(oldPassword.text.toString())!!){
                    Toast.makeText(this, getString(R.string.wrong_old) , Toast.LENGTH_SHORT).show()

                    return@addOnSuccessListener
                }
                FirebaseAuth.getInstance().currentUser.updatePassword(password.text.toString()).addOnCompleteListener { it->
                    if(it.isSuccessful){
                        UserRepository.userUpdatePassword(password.text.toString(),this)
                        finish()
                    }
                    else{
                        try{
                            AuthUtil.reAuthEmail(oldPassword.text.toString()).addOnCompleteListener() {task->
                                if(task.isSuccessful){
                                    FirebaseAuth.getInstance().currentUser.updatePassword(password.text.toString()).addOnCompleteListener { it->
                                        if(it.isSuccessful){
                                            UserRepository.userUpdatePassword(password.text.toString(),this)
                                            finish()
                                        }
                                        else{
                                            Toast.makeText(this, getString(R.string.err_relog) , Toast.LENGTH_SHORT).show()

                                        }
                                    }
                                }else{
                                    AuthUtil.reAuthGoogle(this).addOnCompleteListener {
                                        if(task.isSuccessful){
                                            FirebaseAuth.getInstance().currentUser.updatePassword(password.text.toString()).addOnCompleteListener { it->
                                                if(it.isSuccessful){
                                                    UserRepository.userUpdatePassword(password.text.toString(),this)
                                                    finish()
                                                }
                                                else{

                                                    Toast.makeText(this, getString(R.string.err_relog) , Toast.LENGTH_SHORT).show()
                                                }
                                            }

                                        }else{

                                            Toast.makeText(this, getString(R.string.err_relog) , Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }

                            }
                        }catch(exception:Exception){
                            Toast.makeText(this, getString(R.string.err_relog) , Toast.LENGTH_SHORT).show()
                        }


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