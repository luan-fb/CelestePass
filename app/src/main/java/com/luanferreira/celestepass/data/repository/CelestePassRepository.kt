package com.luanferreira.celestepass.data.repository

import androidx.room.Query
import com.luanferreira.celestepass.data.model.Cliente
import com.luanferreira.celestepass.data.model.Ingresso
import com.luanferreira.celestepass.data.model.Jogo
import com.luanferreira.celestepass.data.model.Setor
import com.luanferreira.celestepass.data.model.TimeCrestResponse
import com.luanferreira.celestepass.data.model.Venda
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface CelestePassRepository {
    fun getAllJogos(): Flow<List<Jogo>>

    suspend fun insertJogo(jogo: Jogo)

    suspend fun getEscudoDoTimePelaApi(timeId: Int): Response<TimeCrestResponse>

    suspend fun deleteJogo(jogo: Jogo)

    fun getJogoPorId(jogoId: Long): Flow<Jogo?>

    fun getVendasDoMes(): Flow<List<Venda>>

    fun getVendasDoAno(): Flow<List<Venda>>

    fun getVendasDoJogo(jogoId: Long): Flow<List<Venda>>

    fun getIngressosCompradosDoJogo(jogoId: Long): Flow<List<Ingresso>>

    fun getClientes(): Flow<List<Cliente>>

    suspend fun insertIngresso(ingresso: Ingresso)

    fun getAllSetores(): Flow<List<Setor>>

    suspend fun insertSetor(setor: Setor)

    suspend fun insertCliente(cliente: Cliente)

    suspend fun deleteCliente(cliente: Cliente)

    suspend fun deleteSetor(setor: Setor)







}
