package com.harash1421.payment_integration.data.repositories

import com.harash1421.payment_integration.data.api.StripeApiService
import com.harash1421.payment_integration.data.models.CustomerApiModel
import com.harash1421.payment_integration.data.models.EphemeralKeyApiModel
import com.harash1421.payment_integration.data.models.PaymentIntentApiModel
import javax.inject.Inject

class StripeRepository @Inject constructor(private val apiService: StripeApiService) {

    suspend fun createCustomer(): CustomerApiModel {
        return apiService.createCustomer()
    }

    suspend fun createEphemeralKey(customerId: String): EphemeralKeyApiModel {
        return apiService.createEphemeralKey(customerId)
    }

    suspend fun createPaymentIntent(
        customerId: String,
        amount: Int,
        currency: String,
        autoPaymentMethodsEnable: Boolean
    ): PaymentIntentApiModel {
        return apiService.createPaymentIntent(
            customerId,
            amount,
            currency,
            autoPaymentMethodsEnable
        )
    }
}
