package com.harash1421.payment_integration.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.harash1421.payment_integration.ui_screens.BraintreeScreen
import com.harash1421.payment_integration.ui_screens.InAppPurchaseScreen
import com.harash1421.payment_integration.ui_screens.MainScreen
import com.harash1421.payment_integration.ui_screens.StripeScreen
import com.harash1421.payment_integration.util.Screens

@Composable
fun SetupNavGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = Screens.Main.route){
        composable(route = Screens.Main.route){
            MainScreen(navController = navController)
        }
        composable(route = Screens.InAppPurchase.route){
            InAppPurchaseScreen(navController = navController)
        }
        composable(route = Screens.Stripe.route){
            StripeScreen(navController = navController)
        }
        composable(route = Screens.Braintree.route){
            BraintreeScreen(navController = navController)
        }
    }
}