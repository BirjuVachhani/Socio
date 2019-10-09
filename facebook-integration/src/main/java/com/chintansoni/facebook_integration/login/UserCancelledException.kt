package com.chintansoni.facebook_integration.login

private const val user_cancelled_exception_message = "User cancelled login action."

object UserCancelledException : Exception(user_cancelled_exception_message)