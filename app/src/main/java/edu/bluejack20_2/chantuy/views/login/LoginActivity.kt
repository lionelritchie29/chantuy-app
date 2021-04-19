package edu.bluejack20_2.chantuy.views.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.common.Scopes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth

import com.google.firebase.ktx.Firebase

import com.google.rpc.context.AttributeContext
import edu.bluejack20_2.chantuy.MainActivity

import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.repositories.UserRepository

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(FirebaseAuth.getInstance().currentUser==null){
            themeAndLogo()
            privacyAndTerms()
            createSignInIntent()
        }else{
            val intent  = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                //                val user = FirebaseAuth.getInstance().currentUser
                // ...



                UserRepository.setUser(FirebaseAuth.getInstance().currentUser.uid,FirebaseAuth.getInstance().currentUser.email, FirebaseAuth.getInstance().currentUser.displayName)

                val intent  = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...

            }
        }
    }

    private fun createSignInIntent(){
//        AuthUI.IdpConfig googleIdp = new AuthUI.IdpConfig.GoogleBuilder().setScopes(Scopes.EMAIL).build()
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().setRequireName(true).build(),
            AuthUI.IdpConfig.GoogleBuilder().build()

        )
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(),RC_SIGN_IN)

    }
    private fun themeAndLogo() {
        val providers = emptyList<AuthUI.IdpConfig>()

        // [START auth_fui_theme_logo]
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
//                .setLogo(R.drawable.my_great_logo)
                .setTheme(R.style.Theme_AppCompat_DayNight) // Set theme
                .build(),
            RC_SIGN_IN)
        // [END auth_fui_theme_logo]
    }

    private fun privacyAndTerms() {
        val providers = emptyList<AuthUI.IdpConfig>()
        // [START auth_fui_pp_tos]
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTosAndPrivacyPolicyUrls(
                    "https://example.com/terms.html",
                    "https://example.com/privacy.html")
                .build(),
            RC_SIGN_IN)
        // [END auth_fui_pp_tos]
    }
    private fun signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                // ...
            }
        // [END auth_fui_signout]
    }
    companion object {

        private const val RC_SIGN_IN = 123
    }
}

