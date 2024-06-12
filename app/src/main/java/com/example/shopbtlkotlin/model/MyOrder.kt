package com.example.shopbtlkotlin.model

import java.io.Serializable

data class MyOrder(
    val id: String = "",
    val status: String = "",
    val address: Address? = null,
    val cartItems: List<CartItem>? = null,
    val paymentMethod: PaymentMethod? = null,
    val total: Double = 0.0
) : Serializable
