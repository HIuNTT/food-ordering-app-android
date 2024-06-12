package com.example.shopbtlkotlin.model

import java.io.Serializable

data class CartItem(
    val foodId: Int = 0,
    val userId: String = "",
    var quantity: Int = 0
) : Serializable
