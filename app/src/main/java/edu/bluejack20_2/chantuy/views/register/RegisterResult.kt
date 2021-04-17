package edu.bluejack20_2.chantuy.views.register

/**
 * Authentication result : success (user details) or error message.
 */
data class RegisterResult(
    val success: RegisterInUserView? = null,
    val error: Int? = null
)