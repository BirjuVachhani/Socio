package com.chintansoni.socio

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chintansoni.facebook_integration.UserDataPermissions.public_profile
import com.chintansoni.facebook_integration.login.FacebookLogin
import com.chintansoni.facebook_integration.login.LoginState
import com.chintansoni.facebook_integration.util.setCallback
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Using Facebook Login Button
        val loginButton = findViewById<LoginButton>(R.id.loginButton)
        loginButton.setPermissions(listOf(public_profile))
        loginButton.setCallback(this) {
            when (it) {
                is LoginState.Success -> {
                    val result: LoginResult = it.result
                }
                is LoginState.Failure -> {
                    // handle exception
                }
            }
        }

        // Using Custom Button
        customButton.setOnClickListener {
            FacebookLogin.login(this) {
                when (it) {
                    is LoginState.Success -> {
                        val result: LoginResult = it.result
                    }
                    is LoginState.Failure -> {
                        // handle exception
                    }
                }
            }
        }

    }
}
