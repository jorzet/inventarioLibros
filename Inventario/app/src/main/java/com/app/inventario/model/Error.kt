package com.app.inventario.model

class Error(
    var error: String?,
    var code: Int
): Throwable()