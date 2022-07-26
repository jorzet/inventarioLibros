package com.app.inventario.interactor

import com.app.inventario.connector.Repository
import com.app.inventario.model.Sell
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SaveSellInteractor {
    private val repository: Repository = Repository()
    fun execute(sell: Sell) {
        GlobalScope.launch {
            repository.saveBook(sell)
        }
    }
}