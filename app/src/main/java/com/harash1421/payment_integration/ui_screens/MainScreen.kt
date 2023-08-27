package com.harash1421.payment_integration.ui_screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.harash1421.payment_integration.util.Screens

@Composable
fun MainScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(verticalArrangement = Arrangement.SpaceAround) {
            ButtonClick(title = "InAppPurchase") {
                navController.navigate(route = Screens.InAppPurchase.route)
            }
            ButtonClick(title = "Stripe") {
                navController.navigate(route = Screens.Stripe.route)
            }
            ButtonClick(title = "Braintree") {
                navController.navigate(route = Screens.Braintree.route)
            }
        }
    }
}

@Composable
fun ButtonClick(title: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier.padding(28.dp).height(58.dp).fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
        contentColor = Color.White,
        backgroundColor = Color.Black
        ),
        onClick = onClick
    ) {
        Text(text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium)
    }
}