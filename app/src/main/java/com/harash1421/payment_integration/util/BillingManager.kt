package com.harash1421.payment_integration.util

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.billingclient.api.*
import com.harash1421.payment_integration.data.models.Product

class BillingManager(context: Context) {

    private val TAG = "BillingManager"

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        // Handle purchase updates. Left for future implementation.
    }

    private val billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases()
        .build()

    init {
        startBillingClientConnection(context)
    }

    private fun startBillingClientConnection(context: Context) {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "BillingClient is ready")
                } else {
                    Log.e(TAG, "Error ${billingResult.responseCode}: ${billingResult.debugMessage}")
                    Toast.makeText(context, "${billingResult.responseCode}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.w(TAG, "BillingService was disconnected, trying to restart")
                Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show()            }
        })
    }

    fun queryAvailableProducts(callback: (List<Product>) -> Unit) {
        val skuList = listOf("shoe_nike_large")
        val params = SkuDetailsParams.newBuilder()
            .setSkusList(skuList)
            .setType(BillingClient.SkuType.INAPP)
            .build()

        billingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && !skuDetailsList.isNullOrEmpty()) {
                val products = skuDetailsList.map { Product(it.sku, it.title, it.description, it.price) }
                callback(products)
            } else {
                Log.e(TAG, "Failed to query products: ${billingResult.debugMessage}")
            }
        }
    }

    fun purchaseProduct(activity: Activity) {
        val skuList = listOf("shoe_nike_large")
        val skuDetailsParams = SkuDetailsParams.newBuilder()
            .setSkusList(skuList)
            .setType(BillingClient.SkuType.INAPP)
            .build()

        billingClient.querySkuDetailsAsync(skuDetailsParams) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && !skuDetailsList.isNullOrEmpty()) {
                val flowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(skuDetailsList[0])
                    .build()
                billingClient.launchBillingFlow(activity, flowParams)
            } else {
                Log.e(TAG, "Failed to initiate purchase flow: ${billingResult.debugMessage}")
            }
        }
    }
}
