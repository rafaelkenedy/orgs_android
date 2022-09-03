package com.rafael.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rafael.orgs.database.AppDatabase
import com.rafael.orgs.databinding.ActivityFormularioProdutoBinding
import com.rafael.orgs.extensions.tentaCarregarImagem
import com.rafael.orgs.extensions.toHash
import com.rafael.orgs.model.Produto
import com.rafael.orgs.ui.dialog.FormImgDialog
import dataStore
import kotlinx.coroutines.launch
import usuarioLogadoPreferences
import java.math.BigDecimal

class FormularioProdutoActivity : AppCompatActivity() {

    private val binding by lazy { ActivityFormularioProdutoBinding.inflate(layoutInflater) }
    private var url: String? = null
    private var produtoId = 0L
    private val produtoDao by lazy { AppDatabase.instancia(this).produtoDao() }
    private val usuarioDao by lazy { AppDatabase.instancia(this).usuarioDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = "Cadastrar produto"
        configuraBotaoSalvar()
        tentaCarregarProduto()
        binding.produtoImagem.setOnClickListener {
            FormImgDialog(this).mostra(url) { img ->
                url = img
                binding.produtoImagem.tentaCarregarImagem(url)
            }
        }
        tentaBuscarProduto()
        lifecycleScope.launch {
            dataStore.data.collect { preferences ->
                preferences[usuarioLogadoPreferences]?.let { usuarioId ->
                    usuarioDao.buscaPorId(usuarioId).collect {

                    }
                }
            }
        }
    }


    private fun tentaBuscarProduto() {


        lifecycleScope.launch {
            produtoDao.buscaPorId(produtoId).collect { produto ->
                title = "Alterar produto"
                if (produto != null) {
                    preencheCampos(produto)
                }
            }
        }
    }

    private fun configuraBotaoSalvar() {
        val botaoSalvar = binding.botaoSalvar

        botaoSalvar.setOnClickListener {
            val produtoNovo = criaProduto()
            lifecycleScope.launch {
                produtoDao.salva(produtoNovo)
                finish()
            }
        }
    }

    private fun tentaCarregarProduto() {
        produtoId = intent.getLongExtra(CHAVE_PRODUTO_ID, produtoId)
    }

    private fun preencheCampos(it: Produto) {

        url = it.imagem

        with(binding) {
            produtoImagem.tentaCarregarImagem(it.imagem)
            formProdutoTietNome.setText(it.nome)
            formProdutoTietDescricao.setText(it.descricao)
            formProdutoTietValor.setText(it.valor.toPlainString())
        }
    }

    private fun criaProduto(): Produto {
        val nome = binding.formProdutoTietNome.text.toString()
        val descricao = binding.formProdutoTietDescricao.text.toString()
        val valorTexto = binding.formProdutoTietValor.text.toString().toHash()
        val valor = if (valorTexto.isBlank()) BigDecimal.ZERO else BigDecimal(valorTexto)

        return Produto(
            id = produtoId,
            nome = nome,
            descricao = descricao,
            valor = valor,
            imagem = url
        )
    }
}