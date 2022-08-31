package com.app.inventario.model

data class Sell(
    val productId: String,
    val clientId: String,
    val date: String,
    val status: String,
    val userName: String
)
