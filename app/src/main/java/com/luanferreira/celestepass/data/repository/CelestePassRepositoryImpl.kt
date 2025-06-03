package com.luanferreira.celestepass.data.repository

import com.luanferreira.celestepass.data.local.* // Importa todos os seus DAOs
import com.luanferreira.celestepass.data.model.Jogo
import kotlinx.coroutines.flow.Flow

// A implementação define "como" o repositório faz as coisas
class CelestePassRepositoryImpl(
    // ✅ AGORA O CONSTRUTOR ACEITA TODOS OS CINCO DAOS ✅
    private val jogoDao: JogoDao,
    private val setorDao: SetorDao,
    private val clienteDao: ClienteDao,
    private val ingressoDao: IngressoDao,
    private val vendaDao: VendaDao
) : CelestePassRepository {

    override fun getAllJogos(): Flow<List<Jogo>> {
        return jogoDao.getAllJogos()
    }

    override suspend fun insertJogo(jogo: Jogo) {
        jogoDao.insert(jogo)
        // No futuro, aqui você também adicionaria o código para salvar no Firestore
    }

    // Implementaremos as outras funções aqui conforme a necessidade
}