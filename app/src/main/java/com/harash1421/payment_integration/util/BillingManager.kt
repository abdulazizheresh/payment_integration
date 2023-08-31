package com.harash1421.payment_integration.util

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BillingManager(private val context: Context) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private lateinit var billingClient: BillingClient
    private lateinit var productDetails: ProductDetails
    private lateinit var purchase: Purchase

    private val demoProductId = "shoe_nike_large"

    private val _productName = MutableStateFlow("Searching...")
    val productName = _productName.asStateFlow()

    private val _buyEnabled = MutableStateFlow(false)
    val buyEnabled = _buyEnabled.asStateFlow()

    private val _consumeEnabled = MutableStateFlow(false)
    val consumeEnabled = _consumeEnabled.asStateFlow()

    private val _statusText = MutableStateFlow("Initializing...")
    val statusText = _statusText.asStateFlow()

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                purchases?.forEach { completePurchase(it) }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                _statusText.value = "Purchase Canceled"
            }
            else -> {
                _statusText.value = "Purchase Error"
            }
        }
    }

    init {
        setupBillingClient()
    }

    private fun setupBillingClient() {
        billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                handleBillingSetupFinished(billingResult)
            }

            override fun onBillingServiceDisconnected() {
                _statusText.value = "Billing Client Connection Lost"
            }
        })
    }

    private fun handleBillingSetupFinished(billingResult: BillingResult) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            _statusText.value = "Billing Client Connected"
            queryProduct(demoProductId)
        } else {
            _statusText.value = "Billing Client Connection Failure"
        }
    }

    fun queryProduct(productId: String) {
        val queryProductDetailsParams = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(productId)
                        .setProductType(
                            BillingClient.ProductType.INAPP
                        )
                        .build()
                )
            )
            .build()

        billingClient.queryProductDetailsAsync(queryProductDetailsParams) { _, productDetailsList ->
            if (productDetailsList.isNotEmpty()) {
                productDetails = productDetailsList[0]
                _productName.value = "Product: ${productDetails.name}"
                _buyEnabled.value = true
            } else {
                _statusText.value = "No Matching Products Found"
                _buyEnabled.value = false
            }
        }
    }

    private fun completePurchase(item: Purchase) {
        purchase = item
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            _buyEnabled.value = false
            _consumeEnabled.value = true
            _statusText.value = "Purchase Completed"
        }
    }

    fun makePurchase(activity: Activity) {
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetails)
                        .build()
                )
            )
            .build()

        billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    fun consumePurchase() {
        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        coroutineScope.launch {
            val result = billingClient.consumePurchase(consumeParams)
            if (result.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                _statusText.value = "Purchase Consumed"
                _buyEnabled.value = true
                _consumeEnabled.value = false
            }
        }
    }

    private fun reloadPurchase() {
        val queryPurchasesParams = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        billingClient.queryPurchasesAsync(queryPurchasesParams, purchasesListener)
    }

    private val purchasesListener = PurchasesResponseListener { _, purchases ->
        if (purchases.isNotEmpty()) {
            purchase = purchases.first()
            _buyEnabled.value = false
            _consumeEnabled.value = true
            _statusText.value = "Previous Purchase Found"
        } else {
            _buyEnabled.value = true
            _consumeEnabled.value = false
        }
    }
}
