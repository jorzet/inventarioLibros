package com.app.inventario.interactor

import com.app.inventario.model.Sell
import com.app.inventario.repository.BooksRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SaveSellInteractor {
    private val repository: BooksRepository = BooksRepository()
    fun execute(sell: Sell) {
        GlobalScope.launch {
            repository.saveBook(sell)
        }
    }
}