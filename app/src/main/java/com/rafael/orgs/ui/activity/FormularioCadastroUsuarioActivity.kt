package com.rafael.orgs.ui.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.rafael.orgs.databinding.ActivityFormularioCadastroUsuarioBinding
import com.rafael.orgs.model.Usuario

class FormularioCadastroUsuarioActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityFormularioCadastroUsuarioBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraBotaoCadastrar()
    }

    private fun configuraBotaoCadastrar() {
        binding.activityFormularioCadastroUsuario.setOnClickListener {
            val novoUsario = criarUsuario()
            finish()
        }

    }

    private fun criarUsuario(): Usuario {
        val usuario = binding.activityFormularioCadastroUsuario.text.toString()
        val nome = binding.activityFormularioCadastroNome.text.toString()
        val senha = binding.activityFormularioCadastroSenha.text.toString()
        return Usuario(usuario, nome, senha)
    }
}