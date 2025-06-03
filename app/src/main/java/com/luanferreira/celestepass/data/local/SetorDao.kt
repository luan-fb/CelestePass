package com.luanferreira.celestepass.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.luanferreira.celestepass.data.model.Setor
import kotlinx.coroutines.flow.Flow


@Dao
interface SetorDao {

    @Insert
    suspend fun insert( setor: Setor)

    @Query("SELECT * FROM setores ORDER BY nome ASC")
    fun getAllSetor(): Flow<List<Setor>>

}