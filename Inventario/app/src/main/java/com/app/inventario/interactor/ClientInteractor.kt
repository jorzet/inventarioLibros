package com.app.inventario.interactor

import com.app.inventario.downloader.ClientDownloader
import com.app.inventario.model.Client
import com.app.inventario.repository.ClientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ClientInteractor {
    private val repository: ClientRepository = ClientRepository()

    suspend fun executeGetClient(clientCode: String, onGetClientListener: ClientDownloader.OnGetClientListener) = withContext(Dispatchers.IO) {
        try {
            val client = repository.getClient(clientCode)
            if (client == null) {
                onGetClientListener.onGetClientError()
            }
            else {
                onGetClientListener.onGetClientSuccess(client)
            }
        } catch (e: Exception) {
            onGetClientListener.onGetClientError()
            e.printStackTrace()
        }
    }

    suspend fun executeSaveClient(client: Client, onSaveClientListener: ClientDownloader.OnSaveClientListener) {
        try {
            val result = repository.saveClient(client)
            if (result.isNullOrEmpty()) {
                onSaveClientListener.onSaveClientError()
            } else {
                onSaveClientListener.onSaveClientSuccess()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onSaveClientListener.onSaveClientError()
        }
    }

}