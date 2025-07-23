package com.luanferreira.celestepass.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.luanferreira.celestepass.data.model.Venda
import kotlinx.coroutines.flow.Flow

@Dao
interface VendaDao {
    @Insert
    suspend fun insert(venda: Venda)

    @Update
    suspend fun update(venda: Venda)

    @Query("""
        SELECT vendas.* FROM vendas 
        INNER JOIN ingressos ON vendas.ingressoId = ingressos.id 
        WHERE ingressos.jogoId = :idDoJogo
    """)
    fun getVendasDoJogo(idDoJogo: Long): Flow<List<Venda>>

    @Query("SELECT * FROM vendas") // Placeholder, crie queries específicas
    fun getTodasAsVendas(): Flow<List<Venda>>

    @Query("SELECT * FROM vendas")
    fun getAllVendas(): Flow<List<Venda>>
}