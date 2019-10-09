package com.chintansoni.facebook_integration.login

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import com.chintansoni.facebook_integration.UserDataPermissions.public_profile
import com.chintansoni.facebook_integration.util.ThreadUtil
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

object FacebookLogin {

    private var callbackManager = CallbackManager.Factory.create()
    private var loginManager = LoginManager.getInstance()

    private val loginStateLiveData = MutableLiveData<LoginState>()

    fun isLoggedIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null && !accessToken.isExpired
    }

    fun login(
        activity: AppCompatActivity,
        permissionList: List<String> = listOf(public_profile),
        callback: (LoginState) -> Unit = {}
    ) {
        login(activity, null, permissionList, callback)
    }

    fun login(
        fragment: Fragment,
        permissionList: List<String> = listOf(public_profile),
        callback: (LoginState) -> Unit = {}
    ) {
        login(null, fragment, permissionList, callback)
    }

    private fun login(
        activity: AppCompatActivity?,
        fragment: Fragment?,
        permissionList: List<String>,
        callback: (LoginState) -> Unit = {}
    ) {

        // Observe LiveData and bind callback
        registerObserver(activity, fragment, callback)

        // Return if login is already initiated
        if (isInitiated()) {
            return
        }

        // Set Initiated State
        setState(LoginState.Initiated)

        // Login with read permissions
        loginManager.registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    setState(LoginState.Success(loginResult))
                }

                override fun onCancel() {
                    setState(LoginState.Failure(UserCancelledException))
                }

                override fun onError(e: FacebookException) {
                    setState(LoginState.Failure(e))
                }
            })

        // Login
        loginWithReadPermissions(activity, fragment, permissionList)
    }

    private fun registerObserver(
        activity: AppCompatActivity?,
        fragment: Fragment?,
        callback: (LoginState) -> Unit = {}
    ) {
        activity?.let {
            loginStateLiveData.observe(it) { loginState ->
                callback.invoke(loginState)
            }
        }
        fragment?.let {
            loginStateLiveData.observe(it) { loginState ->
                callback.invoke(loginState)
            }
        }
    }

    private fun loginWithReadPermissions(
        activity: AppCompatActivity?,
        fragment: Fragment?,
        permissionList: List<String>
    ) {
        activity?.let {
            loginManager.logInWithReadPermissions(it, permissionList)
        }
        fragment?.let {
            loginManager.logInWithReadPermissions(it, permissionList)
        }
    }

    fun logout() {
        loginManager.logOut()
    }

    private fun setState(loginState: LoginState) {
        if (ThreadUtil.isMainThread()) {
            loginStateLiveData.value = loginState
        } else {
            loginStateLiveData.postValue(loginState)
        }
    }

    private fun isInitiated(): Boolean {
        return loginStateLiveData.value == LoginState.Initiated
    }
}
