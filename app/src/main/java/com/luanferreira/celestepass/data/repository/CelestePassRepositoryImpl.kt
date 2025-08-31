package com.luanferreira.celestepass.data.repository

import androidx.room.Transaction
import com.luanferreira.celestepass.data.local.*
import com.luanferreira.celestepass.data.model.Cliente
import com.luanferreira.celestepass.data.model.Ingresso
import com.luanferreira.celestepass.data.model.Jogo
import com.luanferreira.celestepass.data.model.Setor
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

    override suspend fun deleteJogo(jogo: Jogo) {
        jogoDao.deleteJogo(jogo)
    }

    override suspend fun getEscudoDoTimePelaApi(timeId: Int): Response<TimeCrestResponse> {
        return apiService.getTeamDetails(FOOTBALL_DATA_AUTH_TOKEN, timeId)
    }

    override fun getVendasDoMes(): Flow<List<Venda>> {
        return vendaDao.getTodasAsVendas()
    }

    override fun getVendasDoAno(): Flow<List<Venda>> {
        return vendaDao.getTodasAsVendas()
    }

    override suspend fun updateJogo(jogo: Jogo) {
        jogoDao.updateJogo(jogo)
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


    override suspend fun insertIngresso(ingresso: Ingresso) {
        ingressoDao.insert(ingresso)
    }

    override fun getAllSetores(): Flow<List<Setor>> {
        return setorDao.getAllSetores()
    }

    override suspend fun insertSetor(setor: Setor) {
        setorDao.insert(setor)
    }

    override suspend fun insertCliente(cliente: Cliente) {
        clienteDao.insert(cliente)
    }


    override suspend fun deleteCliente(cliente: Cliente) {
        clienteDao.delete(cliente)
    }

    override suspend fun deleteSetor(setor: Setor) {
        setorDao.delete(setor)
    }

    override fun getClientes(): Flow<List<Cliente>> {
        return clienteDao.getAllClientes()
    }

    @Transaction
    override suspend fun registarVenda(venda: Venda, ingressoDoLote: Ingresso) {
        vendaDao.insert(venda)
        ingressoDao.update(ingressoDoLote)
    }

    override fun getAllVendas(): Flow<List<Venda>> {
        return vendaDao.getAllVendas()
    }

    override fun getAllIngressos(): Flow<List<Ingresso>> {
        return ingressoDao.getAllIngressos()
    }

    override suspend fun marcarVendaComoEntregue(venda: Venda) {
        vendaDao.update(venda)
    }

    override suspend fun deleteIngresso(ingresso: Ingresso) {
        ingressoDao.delete(ingresso)
    }

    @Transaction
    override suspend fun deleteVenda(venda: Venda, ingressoDoLote: Ingresso) {
        ingressoDao.update(ingressoDoLote)
        vendaDao.delete(venda)
    }

    override suspend fun getIngressosCountForJogo(jogoId: Long): Int {
        return ingressoDao.getIngressosCountByJogoId(jogoId)
    }

    override suspend fun updateCliente(cliente: Cliente) {
        clienteDao.update(cliente)
    }

    override fun getClienteById(clienteId: Long): Flow<Cliente?> {
        return clienteDao.getClienteById(clienteId)
    }

    override fun getVendasDoCliente(clienteId: Long): Flow<List<Venda>> {
        return vendaDao.getVendasByClienteId(clienteId)
    }

    override suspend fun updateSetor(setor: Setor) {
        setorDao.update(setor)
    }

    override fun getSetorById(setorId: Long): Flow<Setor?> {
        return setorDao.getSetorById(setorId)
    }
}
