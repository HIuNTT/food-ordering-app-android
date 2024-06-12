package com.example.shopbtlkotlin.model

import java.io.Serializable

data class Address(
    val id: String? = null,
    val name: String? = null,
    val phone: String? = null,
    val address: String? = null
): Serializable
