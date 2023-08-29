package com.harash1421.payment_integration.ui_screens

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.harash1421.payment_integration.data.models.Product
import com.harash1421.payment_integration.viewmodel.IAPViewModel

@Composable
fun InAppPurchaseScreen(
    navController: NavHostController,
    activity: Activity,
    viewModel: IAPViewModel = hiltViewModel()
) {
//    val context = LocalContext.current
    val products by viewModel.productDetailsList.observeAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.queryProductDetails()
    }

    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        products.forEach { product ->
            Text(text = product.title)
        }

        Button(onClick = {
            viewModel.purchaseProduct(activity)
        }) {
            Text("Buy Now")
        }
    }
}

//@Composable
//fun ProductItem(product: Product, onBuyClick: () -> Unit) {
//    Button(onClick = onBuyClick) {
//        Text("Buy ${product.title}")
//    }
//}

