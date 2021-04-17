package edu.bluejack20_2.chantuy.views.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import edu.bluejack20_2.chantuy.data.RegisterRepository
import edu.bluejack20_2.chantuy.data.Result

import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.repositories.UserRepository

class RegisterViewModel(private val registerRepository: RegisterRepository) : ViewModel() {

    private val _registerForm = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerForm

    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult

    fun register(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = registerRepository.register(username, password)

        if (result is Result.Success) {

            R.string.login_failed
        } else {
            _registerResult.value = RegisterResult(error = R.string.register_failed)
        }
    }

    fun registerDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _registerForm.value = RegisterFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(username,password)) {
            _registerForm.value = RegisterFormState(passwordError = R.string.invalid_password)
        } else {
            _registerForm.value = RegisterFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check

    private fun isUserNameValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()&& registerValidateEmail(username)
    }

    // A placeholder password validation check
    private fun isPasswordValid(username:String,password: String): Boolean {
        return password.length > 0 && registerValidatePassword(username,password)
    }
    fun registerValidateEmail(email: String):Boolean{
        var exist=false

        UserRepository.getUserByEmail(email, callback = { usersFromRepo ->
            Log.i("Testing",""+usersFromRepo.size)
            exist=usersFromRepo.size>0

        })

        Log.i("Testing","aaa")
        return exist
    }
    fun registerValidatePassword(email: String,password: String):Boolean{
        var validPassword=false

        UserRepository.getUserByEmail(email, callback = { usersFromRepo ->
            for (user in usersFromRepo){
                validPassword=(password==user.password)
            }
        })

        return validPassword
    }
}