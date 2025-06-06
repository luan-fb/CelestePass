package com.luanferreira.celestepass.data.repository

import com.luanferreira.celestepass.data.model.Ingresso
import com.luanferreira.celestepass.data.model.Jogo
import com.luanferreira.celestepass.data.model.TimeCrestResponse
import com.luanferreira.celestepass.data.model.Venda
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface CelestePassRepository {
    fun getAllJogos(): Flow<List<Jogo>>
    suspend fun insertJogo(jogo: Jogo)
    suspend fun getEscudoDoTimePelaApi(timeId: Int): Response<TimeCrestResponse>

    // NOVAS FUNÇÕES PARA OS CARDS DO DASHBOARD
    fun getJogoPorId(jogoId: Long): Flow<Jogo?>
    fun getVendasDoMes(): Flow<List<Venda>> // Exemplo, pode precisar de mais lógica
    fun getVendasDoAno(): Flow<List<Venda>> // Exemplo, pode precisar de mais lógica

    fun getVendasDoJogo(jogoId: Long): Flow<List<Venda>>
    fun getIngressosCompradosDoJogo(jogoId: Long): Flow<List<Ingresso>>

    // Adicionaremos funções para calcular resumo financeiro específico do jogo

}
