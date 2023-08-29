package com.harash1421.payment_integration.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harash1421.payment_integration.data.models.Product
import com.harash1421.payment_integration.util.BillingManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IAPViewModel @Inject constructor(
    private val billingManager: BillingManager
) : ViewModel() {

    private val _productDetailsList: MutableLiveData<List<Product>> = MutableLiveData()
    val productDetailsList: LiveData<List<Product>> get() = _productDetailsList

    fun queryProductDetails() {
        viewModelScope.launch {
            billingManager.queryAvailableProducts { products ->
                _productDetailsList.value = products
            }
        }
    }

    fun purchaseProduct(activity: Activity) {
        viewModelScope.launch {
            billingManager.purchaseProduct(activity)
        }
    }
}
