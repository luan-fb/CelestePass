package com.luanferreira.celestepass.data.repository

import com.luanferreira.celestepass.data.model.Jogo
import kotlinx.coroutines.flow.Flow

interface CelestePassRepository {
    fun getAllJogos(): Flow<List<Jogo>>
    suspend fun insertJogo(jogo: Jogo)
    // ... outras funções para cada ação do app
}