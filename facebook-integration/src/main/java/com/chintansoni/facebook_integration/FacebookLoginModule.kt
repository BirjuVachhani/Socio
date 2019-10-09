package com.chintansoni.facebook_integration

import com.chintansoni.facebook_integration.login.FacebookLogin
import org.koin.dsl.module

val facebookLoginModule = module {
    single {
        FacebookLogin
    }
}