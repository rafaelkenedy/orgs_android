package com.rafael.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu

import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rafael.orgs.R
import com.rafael.orgs.database.AppDatabase
import com.rafael.orgs.databinding.ActivityListaProdutosBinding
import com.rafael.orgs.model.Produto
import com.rafael.orgs.ui.recyclerview.adapter.ListaProdutosAdapter

class ListaProdutosActivity : AppCompatActivity() {


    private val adapter by lazy { ListaProdutosAdapter(this) }
    private val binding by lazy { ActivityListaProdutosBinding.inflate(layoutInflater) }
    private val produtoDao by lazy { AppDatabase.instancia(this).produtoDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraRecyclerView()
        configuraFab()
    }

    override fun onResume() {
        super.onResume()

        val db = AppDatabase.instancia(this)
        val produtoDao = db.produtoDao()
        //produtoDao.delete()
        adapter.atualiza(produtoDao.buscaTodos())

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_lista_produtos, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val produtosOrdenado: List<Produto>? = when (item.itemId) {
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
        }
        produtosOrdenado?.let {
            adapter.atualiza(it)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configuraFab() {
        val fab = binding.floatingActionButton
        fab.setOnClickListener {
            vaiParaFormularioProduto()
        }
    }

    private fun vaiParaFormularioProduto() {
        val intent = Intent(this, FormularioProdutoActivity::class.java)
        startActivity(intent)
    }

    private fun configuraRecyclerView() {

        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        adapter.quandoClicaNoItemListener = { produto ->
            val intent = Intent(this, DetalhesProdutoActivity::class.java).apply {
                putExtra(CHAVE_PRODUTO_ID, produto.id)
            }
            startActivity(intent)
        }
        adapter.quandoClicaEmEditar = {
            Toast.makeText(this, "clicou em editar ${it.nome}", Toast.LENGTH_SHORT).show()
        }
        adapter.quandoClicaEmRemover = {
            Toast.makeText(this, "clicou em remover ${it.nome}", Toast.LENGTH_SHORT).show()
        }
    }
}