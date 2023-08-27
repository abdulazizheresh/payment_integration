package com.harash1421.payment_integration.util

sealed class Screens(val route: String){

    //Main Screen
    data object Main : Screens(route = "main_screen")

    //Stripe Screen
    data object Stripe : Screens(route = "stripe_screen")

    //Braintree Screen
    data object Braintree : Screens(route = "braintree_screen")

    //InAppPurchase Screen
    data object InAppPurchase : Screens(route = "in_app_purchase_screen")
}
