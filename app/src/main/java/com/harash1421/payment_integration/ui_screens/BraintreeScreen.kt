package com.harash1421.payment_integration.ui_screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.braintreepayments.api.DropInClient
import com.harash1421.payment_integration.viewmodel.BraintreeViewModel

@Composable
fun BraintreeScreen(navController: NavHostController, dropInClient: DropInClient) {
    val context = LocalContext.current
    val viewModel: BraintreeViewModel = hiltViewModel()

    val paymentResult = viewModel.paymentResult.collectAsState(initial = null).value

    // Use the passed fragmentActivity directly
    LaunchedEffect(Unit) {
        viewModel.setupClient(dropInClient)
    }

    LaunchedEffect(paymentResult) {
        paymentResult?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            viewModel.startPayment(dropInClient)
        }) {
            Text(text = "Buy Now")
        }
    }
}