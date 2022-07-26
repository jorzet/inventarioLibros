package com.app.inventario.model

data class Sell(
    val clientId: String,
    val productId: String,
    val date: String,
    val status: String
)
