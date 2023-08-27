package com.harash1421.payment_integration.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harash1421.payment_integration.data.models.CustomerApiModel
import com.harash1421.payment_integration.data.models.EphemeralKeyApiModel
import com.harash1421.payment_integration.data.models.PaymentIntentApiModel
import com.harash1421.payment_integration.data.repositories.StripeRepository
import com.stripe.android.paymentsheet.PaymentSheetResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StripeViewModel @Inject constructor(private val stripeRepository: StripeRepository) : ViewModel() {

    // Mutable versions
    private val _customer = MutableStateFlow<CustomerApiModel?>(null)
    private val _ephemeralKey = MutableStateFlow<EphemeralKeyApiModel?>(null)
    private val _paymentIntent = MutableStateFlow<PaymentIntentApiModel?>(null)

    // Exposed as read-only StateFlow
    val customer: StateFlow<CustomerApiModel?> = _customer
    val ephemeralKey: StateFlow<EphemeralKeyApiModel?> = _ephemeralKey
    val paymentIntent: StateFlow<PaymentIntentApiModel?> = _paymentIntent

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun createCustomer() {
        viewModelScope.launch {
            try {
                val response = stripeRepository.createCustomer()
                _customer.emit(response)
            } catch (e: Exception) {
                // Log the exception or provide user feedback
                _error.emit(e.message)
            }
        }
    }

    fun fetchEphemeralKey(customerId: String) {
        viewModelScope.launch {
            try {
                val response = stripeRepository.createEphemeralKey(customerId)
                _ephemeralKey.emit(response)
            } catch (e: Exception) {
                // Log the exception or provide user feedback
                _error.emit(e.message)
            }
        }
    }

    fun createPaymentIntent(
        customerId: String,
        amount: Int = 10000,
        currency: String = "usd",
        autoPaymentMethodsEnable: Boolean = true
    ) {
        viewModelScope.launch {
            try {
                val response = stripeRepository.createPaymentIntent(
                    customerId,
                    amount,
                    currency,
                    autoPaymentMethodsEnable
                )
                _paymentIntent.emit(response)
            } catch (e: Exception) {
                // Log the exception or provide user feedback
                _error.emit(e.message)
            }
        }
    }

    fun handlePaymentResult(context: Context, result: PaymentSheetResult) {
        when (result) {
            PaymentSheetResult.Canceled -> {
                Toast.makeText(context, "Payment Canceled", Toast.LENGTH_SHORT).show()
            }

            PaymentSheetResult.Completed -> {
                Toast.makeText(context, "Payment Successfully", Toast.LENGTH_SHORT).show()
            }

            is PaymentSheetResult.Failed -> {
                Toast.makeText(context, "${result.error}", Toast.LENGTH_SHORT).show()
                Log.e("Payment Error", result.error.toString())
            }
        }
    }
}
