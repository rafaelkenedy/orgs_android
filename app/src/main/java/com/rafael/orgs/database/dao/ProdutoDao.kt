package com.rafael.orgs.database.dao

import androidx.room.*
import com.rafael.orgs.model.Produto

@Dao
interface ProdutoDao {

    @Query("SELECT * FROM Produto")
    fun buscaTodos(): List<Produto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun salva(vararg produto: Produto)

    @Delete
    fun remove(produto: Produto)

//    @Update
//    fun altera(produto: Produto)

    @Query("DELETE FROM Produto")
    fun delete()

    @Query("SELECT * FROM Produto WHERE id = :id")
    fun buscaPorId(id: Long): Produto?

    @Query("SELECT * FROM Produto ORDER BY nome ASC")
    fun buscaTodosOrdenadoPorNomeAsc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY nome DESC")
    fun buscaTodosOrdenadoPorNomeDesc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY descricao ASC")
    fun buscaTodosOrdenadoPorDescricaoAsc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY descricao DESC")
    fun buscaTodosOrdenadoPorDescricaoDesc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY valor ASC")
    fun buscaTodosOrdenadoPorValorAsc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY valor DESC")
    fun buscaTodosOrdenadoPorValorDesc(): List<Produto>
}