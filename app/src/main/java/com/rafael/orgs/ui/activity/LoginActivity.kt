package com.rafael.orgs.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.rafael.orgs.R
import com.rafael.orgs.databinding.ActivityLoginBinding
import com.rafael.orgs.extensions.vaiPara

class LoginActivity : AppCompatActivity() {


    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        configuraBotaoCadastrar()
        configuraBotaoEntrar()
    }

    private fun configuraBotaoCadastrar(){
        binding.activityLoginBotaoEntrar.setOnClickListener {
            binding.activityLoginBotaoCadastrar.setOnClickListener {
                vaiPara(FormularioCadastroUsuarioActivity::class.java)
            }
        }




    }

    private fun configuraBotaoEntrar(){
        binding.activityLoginBotaoEntrar.setOnClickListener {
            val usuario = binding.activityLoginUsuario.text.toString()
            val senha = binding.activityLoginSenha.text.toString()
            Log.i("LoginActivity", "onCreate: $usuario - $senha")
            vaiPara(ListaProdutosActivity::class.java)
        }
    }
}