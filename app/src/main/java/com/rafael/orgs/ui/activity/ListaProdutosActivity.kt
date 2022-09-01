package com.rafael.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rafael.orgs.R
import com.rafael.orgs.database.AppDatabase
import com.rafael.orgs.databinding.ActivityListaProdutosBinding
import com.rafael.orgs.ui.recyclerview.adapter.ListaProdutosAdapter
import kotlinx.coroutines.launch

class ListaProdutosActivity : AppCompatActivity() {

    private val adapter by lazy { ListaProdutosAdapter(this) }
    private val binding by lazy { ActivityListaProdutosBinding.inflate(layoutInflater) }

    //private val produtoDao by lazy { AppDatabase.instancia(this).produtoDao() }
    private val dao by lazy { AppDatabase.instancia(this).produtoDao() }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraRecyclerView()
        configuraFab()
        lifecycleScope.launch {

            dao.buscaTodos().collect { produtos ->

                adapter.atualiza(produtos)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_lista_produtos, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        lifecycleScope.launch {

            when (item.itemId) {

                R.id.menu_lista_produtos_ordenar_nome_asc ->
                    dao.buscaTodosOrdenadoPorNomeAsc()
                R.id.menu_lista_produtos_ordenar_nome_desc ->
                    dao.buscaTodosOrdenadoPorNomeDesc()
                R.id.menu_lista_produtos_ordenar_descricao_asc ->
                    dao.buscaTodosOrdenadoPorDescricaoAsc()
                R.id.menu_lista_produtos_ordenar_descricao_desc ->
                    dao.buscaTodosOrdenadoPorDescricaoDesc()
                R.id.menu_lista_produtos_ordenar_valor_asc ->
                    dao.buscaTodosOrdenadoPorValorAsc()
                R.id.menu_lista_produtos_ordenar_valor_desc ->
                    dao.buscaTodosOrdenadoPorValorDesc()
                R.id.menu_lista_produtos_ordenar_sem_ordem ->
                    dao.buscaTodos()
                else -> null
            }?.collect { produtos ->

                adapter.atualiza(produtos)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun configuraFab() {

        val fab = binding.floatingActionButton
        fab.setOnClickListener {

            vaiParaFormularioProduto()
        }
    }

    private fun vaiParaFormularioProduto(produtoId: Long = 0L) {

        Intent(this, FormularioProdutoActivity::class.java).apply {

            putExtra(CHAVE_PRODUTO_ID, produtoId)
            startActivity(this)
        }
    }

    private fun configuraRecyclerView() {

        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        adapter.quandoClicaNoItemListener = { produto ->

            vaiParaDetalhesProduto(produto.id)
        }
        adapter.quandoClicaEmEditar = { produto ->

            vaiParaFormularioProduto(produto.id)
        }
        adapter.quandoClicaEmRemover = {
            Toast.makeText(
                this@ListaProdutosActivity,
                "removeu ${it.nome}",
                Toast.LENGTH_SHORT
            ).show()
            lifecycleScope.launch {

                dao.remove(it)
            }
        }
    }

    private fun vaiParaDetalhesProduto(produtoId: Long) {
        val intent = Intent(this, DetalhesProdutoActivity::class.java).apply {
            putExtra(CHAVE_PRODUTO_ID, produtoId)
        }
        startActivity(intent)
    }
}