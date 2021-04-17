package edu.bluejack20_2.chantuy.views.register

/**
 * Data validation state of the login form.
 */
data class RegisterFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,

    val isDataValid: Boolean = false
)