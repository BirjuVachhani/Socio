package com.chintansoni.facebook_integration.login

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import com.chintansoni.facebook_integration.UserDataPermissions.public_profile
import com.chintansoni.facebook_integration.util.Promise
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
        permissionList: List<String> = listOf(public_profile)
    ): Promise<LoginResult> = login(activity, null, permissionList)

    fun login(
        fragment: Fragment,
        permissionList: List<String> = listOf(public_profile)
    ): Promise<LoginResult> = login(null, fragment, permissionList)

    private fun login(
        activity: AppCompatActivity?,
        fragment: Fragment?,
        permissionList: List<String>
    ): Promise<LoginResult> {
        val promise = Promise<LoginResult>()

        // Observe LiveData and bind callback
        val owner: LifecycleOwner =
            activity ?: (fragment ?: throw Exception("Activity or Fragment cannot be null"))
        registerObserver(owner, promise)

        // Return if login is already initiated
        if (isInitiated()) {
            return promise
        }

        // Login with read permissions
        loginManager.registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    setState(LoginState(loginResult))
                }

                override fun onCancel() {
                    setState(LoginState(exception = UserCancelledException))
                }

                override fun onError(e: FacebookException) {
                    setState(LoginState(exception = e))
                }
            })

        // Login
        loginWithReadPermissions(activity, fragment, permissionList)
        return promise
    }

    private fun registerObserver(
        owner: LifecycleOwner,
        promise: Promise<LoginResult>
    ) {
        loginStateLiveData.observe(owner) { state ->
            state?.result?.let(promise.success)
            state?.exception?.let(promise.failure)
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
        return loginStateLiveData.value?.isInitiated == true
    }
}
