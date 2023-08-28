package com.harash1421.payment_integration.ui_screens

import android.app.Activity
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.harash1421.payment_integration.viewmodel.IAPViewModel

@Composable
fun InAppPurchaseScreen(
    navController: NavHostController,
    viewModel: IAPViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val products by viewModel.productDetailsList.observeAsState(initial = emptyList())

    // This block will be executed once when the Composable is first shown.
    LaunchedEffect(Unit) {
        viewModel.queryProductDetails()
        Toast.makeText(context, products.toString(), Toast.LENGTH_SHORT).show()
    }

    Column {
        products.forEach { product ->
            Text(text = product.title)
            Button(onClick = {
                viewModel.purchaseProduct(product, context as Activity)
            }) {
                Text("Buy ${product.title}")
            }
        }
    }
}
