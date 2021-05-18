package edu.bluejack20_2.chantuy.views.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthAnonymousUpgradeException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import edu.bluejack20_2.chantuy.MainActivity
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.models.GLOBALS
import edu.bluejack20_2.chantuy.models.User
import edu.bluejack20_2.chantuy.repositories.UserRepository
import edu.bluejack20_2.chantuy.views.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setRegisterListener()
        setLinkListener()
        // Enables Always-on
    }
    fun setRegisterListener(){

        val registerButton: Button =findViewById(R.id.register_button)
        val googleButton: Button =findViewById(R.id.register_google_button)

        googleButton.setOnClickListener{
            initFireBaseUIIntent()
        }
        registerButton.setOnClickListener {
            val userNameText: EditText = findViewById(R.id.register_user_name)
            val emailText: EditText =findViewById(R.id.register_email)
            val passwordText: EditText =findViewById(R.id.register_password)
            val confirmPasswordText: EditText =findViewById(R.id.register_password)
            val errorText: TextView =findViewById(R.id.register_error)
            val userName=userNameText.text.toString()
            val email=emailText.text.toString()
            val password=passwordText.text.toString()
            val confirmPassword=confirmPasswordText.text.toString()
            if(userName.isEmpty()){
                errorText.text=getString(R.string.validate_ue)
                return@setOnClickListener
            }

            if(email.isEmpty()){
                errorText.text=getString(R.string.validate_ee)
                return@setOnClickListener
            }

            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                errorText.text=getString(R.string.validate_ef)

                return@setOnClickListener
            }

            if(password.length<6){
                errorText.text=getString(R.string.validate_pl)

                return@setOnClickListener
            }
            if(!confirmPassword.equals(password)){
                errorText.text=getString(R.string.validate_pcp)
                return@setOnClickListener
            }
            UserRepository.getUserByEmail(emailText.text.toString()).addOnSuccessListener { it->
                val its=it.toObjects(User::class.java)
                if(its.isEmpty()){
                    //registerLogic
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){task->
                        if(task.isSuccessful){
                            FirebaseAuth.getInstance().currentUser.updateProfile(
                                userProfileChangeRequest {
                                    displayName=userName
                                })
                            UserRepository.userRegister(FirebaseAuth.getInstance().currentUser.uid,userName, email, password)
                            val intent  = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            errorText.text=task.exception.toString()
                        }
                    }
                    return@addOnSuccessListener
                }

                errorText.text=getString(R.string.validate_une)
            }
        }
    }

    fun setLinkListener(){
        val linkBtn: TextView=findViewById(R.id.register_link)
        linkBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initFireBaseUIIntent(){
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.Theme_Chantuy) // Set theme
            .setLogo(R.drawable.chantuylogo)
//            .setTosAndPrivacyPolicyUrls(
//                "https://example.com/terms.html",
//                "https://example.com/privacy.html")
            .build(), RC_SIGN_IN
        )
    }

    companion object {

        private const val RC_SIGN_IN = 123
    }
}