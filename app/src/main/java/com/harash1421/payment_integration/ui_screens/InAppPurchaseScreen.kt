package com.harash1421.payment_integration.ui_screens


import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.harash1421.payment_integration.viewmodel.IAPViewModel

@Composable
fun InAppPurchaseScreen(
    navController: NavHostController,
    activity: Activity,
    viewModel: IAPViewModel = hiltViewModel()
) {
    val buyEnabled by viewModel.buyEnabled.observeAsState(false)
    val consumeEnabled by viewModel.consumeEnabled.observeAsState(false)
    val productName by viewModel.productName.observeAsState("")
    val statusText by viewModel.statusText.observeAsState("")

    LaunchedEffect(key1 = Unit) {
        viewModel.queryProduct("shoe_nike_large")
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display the correct or clear icon based on the purchase status
        if (buyEnabled) { // Assuming the user can consume only after purchasing the product
            Icon(Icons.Filled.Check, contentDescription = "Purchased", tint = Color.Green)
        } else {
            Icon(Icons.Filled.Clear, contentDescription = "Not Purchased", tint = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = productName, style = MaterialTheme.typography.h5)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = statusText, style = MaterialTheme.typography.body1)

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (buyEnabled) {
                Button(
                    onClick = {
                        viewModel.makePurchase(activity)
                    }
                ) {
                    Text("Buy")
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            if (consumeEnabled) {
                Button(
                    onClick = {
                        viewModel.consumePurchase()
                    }
                ) {
                    Text("Consume")
                }
            }
        }
    }
}
