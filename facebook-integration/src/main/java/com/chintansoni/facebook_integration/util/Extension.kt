package com.chintansoni.facebook_integration.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import com.chintansoni.facebook_integration.login.FacebookLogin
import com.chintansoni.facebook_integration.login.LoginState
import com.chintansoni.facebook_integration.login.UserCancelledException
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton

fun LoginButton.setCallback(
    activity: AppCompatActivity
): Promise<LoginResult> {
    val promise = Promise<LoginResult>()
    val loginStateLiveData = MutableLiveData<LoginState>()
    loginStateLiveData.observe(activity) { state ->
        state?.result?.let(promise.success)
        state?.exception?.let(promise.failure)
    }
    registerCallback(
        CallbackManager.Factory.create(),
        object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) = promise.success(loginResult)
            override fun onCancel() = promise.failure(UserCancelledException)
            override fun onError(e: FacebookException) = promise.failure(e)
        })
    return promise
}

fun LoginButton.setCallback(
    fragment: Fragment
): Promise<LoginResult> {
    val promise = Promise<LoginResult>()
    val loginStateLiveData = MutableLiveData<LoginState>()
    loginStateLiveData.observe(fragment) { state ->
        state?.result?.let(promise.success)
        state?.exception?.let(promise.failure)
    }
    registerCallback(
        CallbackManager.Factory.create(),
        object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) = promise.success(loginResult)
            override fun onCancel() = promise.failure(UserCancelledException)
            override fun onError(e: FacebookException) = promise.failure(e)
        })
    return promise
}

fun AppCompatActivity.loginWithFacebook(
    permissionList: List<String> = FacebookLogin.commonPermission
): Promise<LoginResult> {
    return FacebookLogin.login(this, permissionList = permissionList)
}

fun Fragment.loginWithFacebook(
    permissionList: List<String> = FacebookLogin.commonPermission
): Promise<LoginResult> {
    return FacebookLogin.login(this, permissionList = permissionList)
}