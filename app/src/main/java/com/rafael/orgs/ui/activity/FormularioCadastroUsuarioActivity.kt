package com.rafael.orgs.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rafael.orgs.database.AppDatabase
import com.rafael.orgs.databinding.ActivityFormularioCadastroUsuarioBinding
import com.rafael.orgs.model.Usuario
import kotlinx.coroutines.launch

class FormularioCadastroUsuarioActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityFormularioCadastroUsuarioBinding.inflate(layoutInflater)
    }

    private val dao by lazy {
        AppDatabase.instancia(this).usuarioDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraBotaoCadastrar()
    }

    private fun configuraBotaoCadastrar() {
        binding.activityFormularioCadastroBotaoCadastrar.setOnClickListener {
            val novoUsario = criarUsuario()
            lifecycleScope.launch {
                try {
                    dao.salva(novoUsario)
                    finish()
                } catch (e: Exception) {

                    Toast.makeText(
                        this@FormularioCadastroUsuarioActivity,
                        "Erro inesperado!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
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