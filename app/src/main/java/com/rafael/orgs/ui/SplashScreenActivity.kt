package com.rafael.orgs.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rafael.orgs.databinding.ActivitySplashScreenBinding

import com.rafael.orgs.ui.activity.ListaProdutosActivity

class SplashScreenActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySplashScreenBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Thread {
            try {
                Thread.sleep(2000)
                mostrarListaDeProdutos()
            } catch (e: Exception) {
                throw e
            }
        }.start()

    }

    private fun mostrarListaDeProdutos() {

        val intent = Intent(this@SplashScreenActivity, ListaProdutosActivity::class.java)
        startActivity(intent)
        finish()
    }


}