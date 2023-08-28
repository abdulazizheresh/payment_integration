package com.harash1421.payment_integration.viewmodel

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.harash1421.payment_integration.data.models.Product
import com.harash1421.payment_integration.util.BillingManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IAPViewModel @Inject constructor(
    private val billingManager: BillingManager
) : ViewModel() {

    val productDetailsList: MutableLiveData<List<Product>> = MutableLiveData()

    fun queryProductDetails() {
        billingManager.queryAvailableProducts { products ->
            productDetailsList.value = products
        }
    }

    fun purchaseProduct(product: Product, activity: Activity) {
        billingManager.purchaseProduct(product, activity)
    }
}