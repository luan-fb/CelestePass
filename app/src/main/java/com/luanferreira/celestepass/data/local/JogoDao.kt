package com.luanferreira.celestepass.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.luanferreira.celestepass.data.model.Jogo
import kotlinx.coroutines.flow.Flow

@Dao
interface JogoDao {
    @Insert
    suspend fun insert(jogo: Jogo)

    @Query("SELECT * FROM jogos ORDER BY data DESC")
    fun getAllJogos(): Flow<List<Jogo>>
}