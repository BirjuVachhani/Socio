package com.chintansoni.facebook_integration.util

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Looper


object ThreadUtil {
    fun isMainThread() = if (VERSION.SDK_INT >= VERSION_CODES.M)
        Looper.getMainLooper().isCurrentThread
    else
        Thread.currentThread() === Looper.getMainLooper().thread
}