package com.luanferreira.celestepass.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.luanferreira.celestepass.data.model.Ingresso
import kotlinx.coroutines.flow.Flow

@Dao
interface IngressoDao {
    @Insert
    suspend fun insert(ingresso: Ingresso)

    @Query("SELECT * FROM ingressos WHERE jogoId = :idDoJogo")
    fun getIngressosDoJogo(idDoJogo: Long): Flow<List<Ingresso>>


}
