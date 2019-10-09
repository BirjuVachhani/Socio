package com.chintansoni.facebook_integration.util

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import com.chintansoni.facebook_integration.login.LoginState
import com.chintansoni.facebook_integration.login.UserCancelledException
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton

/**
 *
 */
fun LoginButton.setCallback(
    activity: AppCompatActivity,
    callback: (LoginState) -> Unit
) {
    val loginStateLiveData = MutableLiveData<LoginState>()
    loginStateLiveData.observe(activity) { loginState ->
        callback.invoke(loginState)
    }
    registerCallback(
        CallbackManager.Factory.create(),
        object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                callback.invoke(LoginState.Success(loginResult))
            }

            override fun onCancel() {
                callback.invoke(LoginState.Failure(UserCancelledException))
            }

            override fun onError(e: FacebookException) {
                callback.invoke(LoginState.Failure(e))
            }
        })
}