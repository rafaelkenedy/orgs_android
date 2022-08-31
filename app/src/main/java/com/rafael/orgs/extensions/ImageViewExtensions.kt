package com.rafael.orgs.extensions

import android.widget.ImageView
import coil.load
import com.rafael.orgs.R

fun ImageView.tentaCarregarImagem(url: String? = null){
    load(url) {
        fallback(R.drawable.erro)
        error(R.drawable.erro)
    }

}