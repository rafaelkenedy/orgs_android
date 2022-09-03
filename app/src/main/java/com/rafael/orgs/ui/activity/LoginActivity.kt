package com.rafael.orgs.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.rafael.orgs.database.AppDatabase
import com.rafael.orgs.databinding.ActivityLoginBinding
import com.rafael.orgs.extensions.vaiPara
import dataStore
import kotlinx.coroutines.launch
import usuarioLogadoPreferences

class LoginActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val usuarioDao by lazy {
        AppDatabase.instancia(this).usuarioDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        configuraBotaoCadastrar()
        configuraBotaoEntrar()
    }

    private fun configuraBotaoCadastrar() {
        binding.activityLoginBotaoCadastrar.setOnClickListener {
            binding.activityLoginBotaoCadastrar.setOnClickListener {
                vaiPara(FormularioCadastroUsuarioActivity::class.java)
            }
        }


    }

    private fun configuraBotaoEntrar() {
        binding.activityLoginBotaoEntrar.setOnClickListener {
            val usuario = binding.activityLoginUsuario.text.toString()
            val senha = binding.activityLoginSenha.text.toString()
            lifecycleScope.launch {

                usuarioDao.autentica(usuario, senha)?.let { usuario ->
                    dataStore.edit { preferences ->
                        preferences[usuarioLogadoPreferences] = usuario.id

                    }
                    vaiPara(ListaProdutosActivity::class.java) {

                    }
                } ?: Toast.makeText(this@LoginActivity, "Falha na autenticação", Toast.LENGTH_SHORT)
                    .show()
            }


        }
    }


}