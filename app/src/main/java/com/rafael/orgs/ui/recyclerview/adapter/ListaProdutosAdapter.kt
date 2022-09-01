package com.rafael.orgs.ui.recyclerview.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.rafael.orgs.R
import com.rafael.orgs.databinding.ProdutoItemBinding
import com.rafael.orgs.extensions.tentaCarregarImagem
import com.rafael.orgs.model.Produto
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class ListaProdutosAdapter(
    private val context: Context,

    produtos: List<Produto> = emptyList(),
    var quandoClicaNoItemListener: (produto: Produto) -> Unit = {},
    var quandoClicaEmEditar: (produto: Produto) -> Unit = {},
    var quandoClicaEmRemover: (produto: Produto) -> Unit = {}

) : RecyclerView.Adapter<ListaProdutosAdapter.ViewHolder>() {

    private val produtos = produtos.toMutableList()

    inner class ViewHolder(binding: ProdutoItemBinding) : RecyclerView.ViewHolder(binding.root),
        PopupMenu.OnMenuItemClickListener {

        private lateinit var produto: Produto
        private val nome = binding.produtoItemNome
        private val descricao = binding.produtoItemDescricao
        private val valor = binding.produtoItemValor
        private val img = binding.produtoIv


        init {

//            itemView.setOnLongClickListener {
//                PopupMenu(context, itemView).apply {
//                    menuInflater.inflate(
//                        R.menu.menu_detalhes_produto,
//                        menu
//                    )
//                    setOnMenuItemClickListener(this@ViewHolder)
//                }.show()
//                true
//            }

            itemView.setOnClickListener {
                if (::produto.isInitialized) {
                    quandoClicaNoItemListener(produto)
                }
            }

            itemView.setOnCreateContextMenuListener { menu, _, _ ->

                menu.add(Menu.NONE, 1, Menu.NONE, "editar").setOnMenuItemClickListener {

                    quandoClicaEmEditar(produto)
                    true
                }
                menu.add(Menu.NONE, 2, Menu.NONE, "remover").setOnMenuItemClickListener {

                    quandoClicaEmRemover(produto)
                    true
                }
            }
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            item?.let {
                when (it.itemId) {
                    R.id.menu_detalhes_produto_editar -> {

                        quandoClicaEmEditar(produto)
                    }
                    R.id.menu_detalhes_produto_remover -> {

                        quandoClicaEmRemover(produto)
                    }
                }
            }
            return true
        }


        fun vincula(produto: Produto) {
            this.produto = produto
            nome.text = produto.nome
            descricao.text = produto.descricao
            val valorEmMoeda: String = formataParaMoedaBrasileira(produto.valor)
            valor.text = valorEmMoeda
            img.tentaCarregarImagem(produto.imagem)
        }

        private fun formataParaMoedaBrasileira(valor: BigDecimal): String {
            val formatador = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
            return formatador.format(valor)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProdutoItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produto = produtos[position]
        holder.vincula(produto)
    }

    override fun getItemCount(): Int = produtos.size
    fun atualiza(produtos: List<Produto>) {
        this.produtos.clear()
        this.produtos.addAll(produtos)
        notifyDataSetChanged()
    }


}
