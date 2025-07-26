package com.luanferreira.celestepass.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.luanferreira.celestepass.data.model.Ingresso
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

    @Delete
    suspend fun delete(venda: Venda)

    @Query("SELECT * FROM vendas WHERE clienteId = :clienteId") // Corrigido para buscar vendas, não o count
    fun getVendasByClienteId(clienteId: Long): Flow<List<Venda>>
}