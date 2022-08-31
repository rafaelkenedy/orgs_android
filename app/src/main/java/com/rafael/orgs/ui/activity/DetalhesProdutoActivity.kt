package com.rafael.orgs.ui.activity


import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.rafael.orgs.R
import com.rafael.orgs.database.AppDatabase
import com.rafael.orgs.databinding.ActivityDetalheProdutoBinding
import com.rafael.orgs.extensions.tentaCarregarImagem
import com.rafael.orgs.model.Produto
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
    }

    override fun onResume() {
        super.onResume()
        buscaProduto()
    }

    private fun buscaProduto() {
        produto = produtoDao.buscaPorId(produtoId)
        produto?.let { produto ->
            preencheCampos(produto)
        } ?: finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detalhes_produto, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_detalhes_produto_remover -> {

                produto?.let { produtoDao.remove(it) }
                finish()
            }
            R.id.menu_detalhes_produto_editar -> {
                Intent(this, FormularioProdutoActivity::class.java).apply {
                    putExtra(CHAVE_PRODUTO_ID, produtoId)
                    startActivity(this)
                }
            }

        }

        return super.onOptionsItemSelected(item)
    }



    private fun formataParaMoedaBrasileira(valor: BigDecimal): String {
        val formatador = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
        return formatador.format(valor)
    }

    private fun tentaCarregarProduto() {

        produtoId = intent.getLongExtra(CHAVE_PRODUTO_ID, 0L)
//        intent.getParcelableExtra<Produto>(CHAVE_PRODUTO)?.let { produtoCarregado ->
//            produtoId = produtoCarregado.id
//        } ?: finish()
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





