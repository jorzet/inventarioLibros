package com.app.inventario.model

data class User (
    var id: Int,
    var email: String,
    var password: String,
    var nick: String,
    var completeName: String
    )