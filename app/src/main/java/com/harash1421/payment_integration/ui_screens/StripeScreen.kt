package com.harash1421.payment_integration.ui_screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.harash1421.payment_integration.viewmodel.StripeViewModel
import com.stripe.android.PaymentConfiguration
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.stripe.android.paymentsheet.PaymentSheetContract
import kotlinx.coroutines.launch

@Composable
fun StripeScreen(navController: NavHostController, viewModel: StripeViewModel = hiltViewModel()) {
    val customer by viewModel.customer.collectAsState()
    val ephemeralKey by viewModel.ephemeralKey.collectAsState()
    val paymentIntent by viewModel.paymentIntent.collectAsState()
    val error by viewModel.error.collectAsState()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val stripeLauncher = rememberLauncherForActivityResult(
        contract = PaymentSheetContract(),
        onResult = {
            viewModel.handlePaymentResult(context, it)
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Payment Screen") })
        },
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Customer ID: ${customer?.id ?: "Not fetched"}")
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Ephemeral Key ID: ${ephemeralKey?.id ?: "Not fetched"}")
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Payment Intent ID: ${paymentIntent?.client_secret ?: "Not fetched"}")
            Spacer(modifier = Modifier.height(16.dp))

            if (error != null) {
                Text(text = "Error: $error", color = MaterialTheme.colors.error)
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(onClick = {
                coroutineScope.launch {
                    viewModel.createCustomer()
                }
            }) {
                Text(text = "Create Customer")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                coroutineScope.launch {
                    customer?.id?.let { viewModel.fetchEphemeralKey(it) }
                }
            }) {
                Text(text = "Fetch Ephemeral Key")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                coroutineScope.launch {
                    customer?.id?.let {id ->
                        viewModel.createPaymentIntent(id)
                    }
                }
            }) {
                Text(text = "Create Payment Intent")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                paymentIntent?.client_secret?.let { clientSecret ->
                    val args = PaymentSheetContract.Args.createPaymentIntentArgs(clientSecret)
                    stripeLauncher.launch(args)
                }

            }) {
                Text(text = "Open Stripe PaymentSheet")
            }
        }
    }
}

@Composable
fun rememberPaymentLauncher(
    callback: PaymentLauncher.PaymentResultCallback
): PaymentLauncher {
    val config = PaymentConfiguration.getInstance(LocalContext.current)
    return PaymentLauncher.rememberLauncher(
        publishableKey = config.publishableKey,
        stripeAccountId = config.stripeAccountId,
        callback = callback
    )
}

