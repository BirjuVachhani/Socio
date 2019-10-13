package com.chintansoni.facebook_integration.login

import com.facebook.login.LoginResult

/*sealed class LoginState {
    object Initiated : LoginState()
    data class Success(val result: LoginResult) : LoginState()
    data class Failure(val exception: Exception) : LoginState()
}*/

data class LoginState(
    val result: LoginResult? = null,
    val exception: Exception? = null,
    var isInitiated: Boolean = false
)