package com.chintansoni.facebook_integration.util

/*
 * Created by Birju Vachhani on 13 October 2019
 * Copyright © 2019 socio. All rights reserved.
 */

class Promise<T> internal constructor() {

    internal var success: (T) -> Unit = {}
    internal var failure: (Throwable) -> Unit = {}

    infix fun onSuccess(func: (T) -> Unit) = apply {
        success = func
    }

    infix fun onFailure(func: (Throwable) -> Unit) = apply {
        failure = func
    }
}