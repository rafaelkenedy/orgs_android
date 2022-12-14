package com.rafael.orgs.ui.activity


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rafael.orgs.R
import com.rafael.orgs.database.AppDatabase
import com.rafael.orgs.databinding.ActivityDetalheProdutoBinding
import com.rafael.orgs.extensions.tentaCarregarImagem
import com.rafael.orgs.model.Produto
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class DetalhesProdutoActivity : AppCompatActivity() {

    private var produtoId: Long = 0L
    private var produto: Produto? = null
    private val binding by lazy { ActivityDetalheProdutoBinding.inflate(layoutInflater) }
    val produtoDao by lazy { AppDatabase.instancia(this).produtoDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        tentaCarregarProduto()
        buscaProduto()
    }

    private fun buscaProduto() {

        lifecycleScope.launch {

            produtoDao.buscaPorId(produtoId).collect {

                produto = it
                produto?.let {

                    preencheCampos(it)
                } ?: finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_detalhes_produto, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.menu_detalhes_produto_remover -> {

                lifecycleScope.launch {

                    produto?.let { produtoDao.remove(it) }
                    finish()
                }
            }
            R.id.menu_detalhes_produto_editar -> {

                vaiParaFormularioProduto()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun vaiParaFormularioProduto() {
        Intent(this, FormularioProdutoActivity::class.java).apply {

            putExtra(CHAVE_PRODUTO_ID, produtoId)
            startActivity(this)
        }
    }

    private fun formataParaMoedaBrasileira(valor: BigDecimal): String {

        val formatador = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
        return formatador.format(valor)
    }

    private fun tentaCarregarProduto() {

        produtoId = intent.getLongExtra(CHAVE_PRODUTO_ID, produtoId)
    }

    private fun preencheCampos(produto: Produto) {

        with(binding) {

            cabecalhoTv.text = produto.nome
            detalhesTv.text = produto.descricao
            valorTv.text = formataParaMoedaBrasileira(produto.valor)
            produtoDetalheIv.tentaCarregarImagem(produto.imagem)
        }
    }
}





