package com.harash1421.payment_integration.data.api

import com.google.gson.GsonBuilder
import com.harash1421.payment_integration.util.Info
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Info.url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val stripeApiService: StripeApiService by lazy {
        retrofit.create(StripeApiService::class.java)
    }
}
