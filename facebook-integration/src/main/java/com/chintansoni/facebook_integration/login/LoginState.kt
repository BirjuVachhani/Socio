package com.chintansoni.facebook_integration.login

import com.facebook.login.LoginResult

sealed class LoginState {
    object Initiated : LoginState()
    data class Success(val result: LoginResult) : LoginState()
    data class Failure(val exception: Exception) : LoginState()
}