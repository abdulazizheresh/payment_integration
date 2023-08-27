package com.harash1421.payment_integration.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.harash1421.payment_integration.ui_screens.StripeScreen
import com.harash1421.payment_integration.util.Screens

@Composable
fun SetupNavGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = Screens.Main.route){
        composable(route = Screens.Stripe.route){
            StripeScreen()
        }
    }
}