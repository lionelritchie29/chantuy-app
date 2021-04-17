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
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse

import com.google.rpc.context.AttributeContext
import edu.bluejack20_2.chantuy.MainActivity

import edu.bluejack20_2.chantuy.R

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themeAndLogo()
        privacyAndTerms()
        createSignInIntent()
        val intent  = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun createSignInIntent(){
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
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

