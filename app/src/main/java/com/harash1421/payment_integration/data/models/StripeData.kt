package com.harash1421.payment_integration.data.models

data class CustomerApiModel(
    val id: String,
    val email: String?,
    val created: Long
)


data class EphemeralKeyApiModel(
    val id: String,
    val created: Long,
    val expires: Long,
    val secret: String,
    val associated_objects: List<AssociatedObject>
)

data class AssociatedObject(
    val id: String,
    val type: String
)

data class PaymentIntentApiModel(
    val id: String,
    val amount: Int,
    val currency: String,
    val created: Long,
    val status: String,
    val client_secret: String?
)