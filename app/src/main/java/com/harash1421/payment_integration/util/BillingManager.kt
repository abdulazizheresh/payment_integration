package com.harash1421.payment_integration.util

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetailsParams
import com.harash1421.payment_integration.data.models.Product

class BillingManager(private val context: Context) {

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        // Handle purchase updates.
        // For simplicity, this is left out for now.
    }

    private val billingClient = BillingClient.newBuilder(context)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases()
        .build()

    init {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here if needed.
                }
            }
            override fun onBillingServiceDisconnected() {
                // Handle disconnections, possibly trying to restart the connection.
            }
        })
    }

    fun queryAvailableProducts(callback: (List<Product>) -> Unit) {
        val skuList = listOf("shoe_nike_large")
        val params = SkuDetailsParams.newBuilder().setSkusList(skuList).setType(BillingClient.SkuType.INAPP).build()

        billingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && !skuDetailsList.isNullOrEmpty()) {
                val products = skuDetailsList.map { Product(it.sku, it.title, it.description, it.price) }
                callback(products)
            }
        }
    }

    fun purchaseProduct(product: Product, activity: Activity) {
        val skuDetailsParams = SkuDetailsParams.newBuilder()
            .setSkusList(listOf(product.productId))
            .setType(BillingClient.SkuType.INAPP)
            .build()

        billingClient.querySkuDetailsAsync(skuDetailsParams) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && !skuDetailsList.isNullOrEmpty()) {
                val flowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(skuDetailsList[0])
                    .build()
                billingClient.launchBillingFlow(activity, flowParams)
            }
        }
    }
}
