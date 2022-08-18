package com.app.inventario.downloader

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.app.inventario.R
import com.app.inventario.interactor.ClientInteractor
import com.app.inventario.model.Client
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class ClientDownloader {
    companion object {
        const val url = "http://libros2022.ddns.net/biblioteca/lectores/"
        const val extension = ".jpg"
    }

    interface OnShowImageListener {
        fun onImageLoaded()
        fun onImageError()
    }

    interface OnGetClientListener {
        fun onGetClientSuccess(client: Client)
        fun onGetClientError()
    }

    interface OnSaveClientListener {
        fun onSaveClientSuccess()
        fun onSaveClientError()
    }
}

    fun ImageView.showPlaceHolder(): ImageView {
        this.setImageResource(R.drawable.ic_launcher_background)
        return this
    }

    fun ImageView.showImage(client: String, onShowImageListener: ClientDownloader.OnShowImageListener): ImageView {
        val url = ClientDownloader.url + client + ClientDownloader.extension
        val picasso = Picasso.Builder(context)
            .listener { picasso, uri, exception -> {
                exception.printStackTrace()
            }}
            .build()

        picasso.load(url).fit().centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(this, object: Callback {
                override fun onSuccess() {
                    onShowImageListener.onImageLoaded()
                }

                override fun onError() {
                    onShowImageListener.onImageError()
                }
            })
        /*Picasso.with(context).load(url).fit().centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(this, object: Callback {
                override fun onSuccess() {
                    onShowImageListener.onImageLoaded()
                }

                override fun onError() {
                    onShowImageListener.onImageError()
                }
            })*/
        return this
    }

    suspend fun TextView.showName(clientCode: String): TextView {
        ClientInteractor().executeGetClient(clientCode, object: ClientDownloader.OnGetClientListener {
            override fun onGetClientSuccess(client: Client) {
                text = client.clientName
                visibility = View.VISIBLE
            }

            override fun onGetClientError() {
                //Toast.makeText(context, "No se pudo obtener el nombre el cliente ingresado", Toast.LENGTH_LONG).show()
            }
        })
        return this
    }