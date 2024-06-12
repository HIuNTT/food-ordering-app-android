package com.example.shopbtlkotlin.model

import java.io.Serializable

data class Food(
    var id: Int = 0,
    var title: String = "",
    var description: String = "",
    var picUrl: String = "",
    var price: Double = 0.0,
    var rating: Double = 0.0,
    var categoryId: Int = 0,
    var isPopular: Boolean = false
): Serializable
