package com.harash1421.payment_integration

import android.app.Application
import com.harash1421.payment_integration.util.Info
import com.stripe.android.PaymentConfiguration
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        PaymentConfiguration.init(applicationContext, Info.stripePublisherKey)
    }
}