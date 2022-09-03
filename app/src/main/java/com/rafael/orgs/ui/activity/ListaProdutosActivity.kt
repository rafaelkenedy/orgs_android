package com.rafael.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.rafael.orgs.R
import com.rafael.orgs.database.AppDatabase
import com.rafael.orgs.databinding.ActivityListaProdutosBinding
import com.rafael.orgs.extensions.vaiPara
import com.rafael.orgs.ui.recyclerview.adapter.ListaProdutosAdapter
import dataStore
import kotlinx.coroutines.launch
import usuarioLogadoPreferences


class ListaProdutosActivity : AppCompatActivity() {

    private val adapter by lazy { ListaProdutosAdapter(this) }
    private val binding by lazy { ActivityListaProdutosBinding.inflate(layoutInflater) }


    //private val produtoDao by lazy { AppDatabase.instancia(this).produtoDao() }
    private val produtoDao by lazy { AppDatabase.instancia(this).produtoDao() }
    private val usuarioDao by lazy { AppDatabase.instancia(this).usuarioDao() }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraRecyclerView()
        configuraFab()
        lifecycleScope.launch {

            launch {
                produtoDao.buscaTodos().collect { produtos ->

                    adapter.atualiza(produtos)
                }
            }

            launch {
                dataStore.data.collect { preferences ->
                    preferences[usuarioLogadoPreferences]?.let { usuarioId ->
                        launch {
                            usuarioDao.buscaPorId(usuarioId).collect {

                            }
                        }
                    } ?: vaiParaLogin()
                }
            }
        }
    }

    private fun vaiParaLogin() {
        vaiPara(LoginActivity::class.java)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_lista_produtos, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when (item.itemId) {
            R.id.menu_lista_produtos_sair_do_app -> {
                lifecycleScope.launch {
                    dataStore.edit { preferences ->
                        preferences.remove(usuarioLogadoPreferences)
                    }
                }
            }
        }

        lifecycleScope.launch {
            when (item.itemId) {

                R.id.menu_lista_produtos_ordenar_nome_asc ->
                    produtoDao.buscaTodosOrdenadoPorNomeAsc()
                R.id.menu_lista_produtos_ordenar_nome_desc ->
                    produtoDao.buscaTodosOrdenadoPorNomeDesc()
                R.id.menu_lista_produtos_ordenar_descricao_asc ->
                    produtoDao.buscaTodosOrdenadoPorDescricaoAsc()
                R.id.menu_lista_produtos_ordenar_descricao_desc ->
                    produtoDao.buscaTodosOrdenadoPorDescricaoDesc()
                R.id.menu_lista_produtos_ordenar_valor_asc ->
                    produtoDao.buscaTodosOrdenadoPorValorAsc()
                R.id.menu_lista_produtos_ordenar_valor_desc ->
                    produtoDao.buscaTodosOrdenadoPorValorDesc()
                R.id.menu_lista_produtos_ordenar_sem_ordem ->
                    produtoDao.buscaTodos()
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

                produtoDao.remove(it)
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