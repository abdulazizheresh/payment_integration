package com.harash1421.payment_integration.data.api

import com.harash1421.payment_integration.data.models.CustomerApiModel
import com.harash1421.payment_integration.data.models.EphemeralKeyApiModel
import com.harash1421.payment_integration.data.models.PaymentIntentApiModel
import com.harash1421.payment_integration.util.Info
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

private const val SECRET = Info.stripeSecretKey

interface StripeApiService {
    @Headers(
        "Authorization: Bearer $SECRET",
        "Stripe-Version: 2022-11-15"
    )
    @POST("v1/customers")
    suspend fun createCustomer() : CustomerApiModel

    @Headers(
        "Authorization: Bearer $SECRET",
        "Stripe-Version: 2022-11-15"
    )
    @POST("v1/ephemeral_keys")
    suspend fun createEphemeralKey(
        @Query("customer") customerId: String
    ): EphemeralKeyApiModel

    @Headers(
        "Authorization: Bearer $SECRET"
    )
    @POST("v1/payment_intents")
    suspend fun createPaymentIntent(
        @Query("customer") customerId: String,
        @Query("amount") amount: Int,
        @Query("currency") currency: String,
        @Query("automatic_payment_methods[enabled]") autoPaymentMethodsEnable: Boolean,
    ): PaymentIntentApiModel

}