package com.rafael.orgs.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.rafael.orgs.databinding.FormularioImagemBinding
import com.rafael.orgs.extensions.tentaCarregarImagem

class FormImgDialog(private val context: Context) {

    fun mostra(
        urlPadrao: String? = null,
        quandoImagemCarregada: (img: String) -> Unit
    ) {
        FormularioImagemBinding.inflate(LayoutInflater.from(context)).apply {
            urlPadrao?.let {
                formIv.tentaCarregarImagem(it)
                tietUrlImagem.setText(it)
            }

            formBtnCarregar.setOnClickListener {
                val url = tietUrlImagem.text.toString()
                formIv.tentaCarregarImagem(url)
            }
            AlertDialog.Builder(context)
                .setPositiveButton("Confirmar") { _, _ ->
                    val url = tietUrlImagem.text.toString()
                    quandoImagemCarregada(url)
                }
                .setView(root)
                .setNegativeButton("Cancelar") { _, _ -> }
                .show()
        }
    }
}