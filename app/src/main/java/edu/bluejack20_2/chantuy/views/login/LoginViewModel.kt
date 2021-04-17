package edu.bluejack20_2.chantuy.views.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import edu.bluejack20_2.chantuy.data.LoginRepository
import edu.bluejack20_2.chantuy.data.Result

import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.repositories.UserRepository

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.login_invalid_username)

        } else if (!isPasswordValid(username,password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.login_invalid_password)

        } else {
            _loginForm.value = LoginFormState(isDataValid = true)

        }
    }

    // A placeholder username validation check

    private fun isUserNameValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()&& loginValidateEmail(username)
    }

    // A placeholder password validation check
    private fun isPasswordValid(username:String,password: String): Boolean {
        return password.length > 0 && loginValidatePassword(username,password)
    }
    fun loginValidateEmail(email: String):Boolean{
        var exist=false

        UserRepository.getUserByEmail(email, callback = { usersFromRepo ->
            Log.i("Testing",""+usersFromRepo.size)
            exist=usersFromRepo.size>0

        })

        Log.i("Testing","aaa")
        return exist
    }
    fun loginValidatePassword(email: String,password: String):Boolean{
        var validPassword=false

        UserRepository.getUserByEmail(email, callback = { usersFromRepo ->
            for (user in usersFromRepo){
                validPassword=(password==user.password)
            }
        })

        return validPassword
    }
}