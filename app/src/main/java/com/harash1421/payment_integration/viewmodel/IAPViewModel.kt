package com.harash1421.payment_integration.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.harash1421.payment_integration.util.BillingManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IAPViewModel @Inject constructor(
    private val billingManager: BillingManager
) : ViewModel() {

    // LiveData for UI
    val productName: LiveData<String> = billingManager.productName.asLiveData()
    val buyEnabled: LiveData<Boolean> = billingManager.buyEnabled.asLiveData()
    val consumeEnabled: LiveData<Boolean> = billingManager.consumeEnabled.asLiveData()
    val statusText: LiveData<String> = billingManager.statusText.asLiveData()

    fun queryProduct(productId: String) = viewModelScope.launch {
        billingManager.queryProduct(productId)
    }

    fun makePurchase(activity: Activity) = viewModelScope.launch {
        billingManager.makePurchase(activity)
    }

    fun consumePurchase() = viewModelScope.launch {
        billingManager.consumePurchase()
    }
}
