package com.luanferreira.celestepass.data.repository

import com.luanferreira.celestepass.data.local.*
import com.luanferreira.celestepass.data.model.Ingresso
import com.luanferreira.celestepass.data.model.Jogo
import com.luanferreira.celestepass.data.model.TimeCrestResponse
import com.luanferreira.celestepass.data.model.Venda // Importe Venda
import com.luanferreira.celestepass.data.remote.ApiService
import com.luanferreira.celestepass.data.remote.FOOTBALL_DATA_AUTH_TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow // Para exemplos simples de fluxo
import retrofit2.Response
import java.util.Calendar

class CelestePassRepositoryImpl(
    private val jogoDao: JogoDao,
    private val setorDao: SetorDao,
    private val clienteDao: ClienteDao,
    private val ingressoDao: IngressoDao,
    private val vendaDao: VendaDao,
    private val apiService: ApiService
) : CelestePassRepository {

    override fun getAllJogos(): Flow<List<Jogo>> {
        return jogoDao.getAllJogos()
    }

    override suspend fun insertJogo(jogo: Jogo) {
        jogoDao.insert(jogo)
    }

    override suspend fun getEscudoDoTimePelaApi(timeId: Int): Response<TimeCrestResponse> {
        return apiService.getTeamDetails(FOOTBALL_DATA_AUTH_TOKEN, timeId)
    }

    // EXEMPLO SIMPLES - A LÓGICA REAL SERÁ MAIS COMPLEXA E USARÁ OS DAOs
    // Você precisará de queries nos seus DAOs para buscar vendas por período
    override fun getVendasDoMes(): Flow<List<Venda>> {
        // Lógica para buscar vendas do mês atual usando vendaDao
        // Exemplo muito simplificado:
        return vendaDao.getTodasAsVendas() // Substitua por uma query real
    }

    override fun getVendasDoAno(): Flow<List<Venda>> {
        // Lógica para buscar vendas do ano atual usando vendaDao
        // Exemplo muito simplificado:
        return vendaDao.getTodasAsVendas() // Substitua por uma query real

    }

    override fun getJogoPorId(jogoId: Long): Flow<Jogo?> {
        return jogoDao.getJogoPorId(jogoId)
    }

    override fun getVendasDoJogo(jogoId: Long): Flow<List<Venda>> {
        return vendaDao.getVendasDoJogo(jogoId)
    }

    override fun getIngressosCompradosDoJogo(jogoId: Long): Flow<List<Ingresso>> {
        return ingressoDao.getIngressosDoJogo(jogoId)
    }

}
