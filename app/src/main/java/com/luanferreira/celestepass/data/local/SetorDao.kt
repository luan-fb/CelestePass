package com.luanferreira.celestepass.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.luanferreira.celestepass.data.model.Setor
import kotlinx.coroutines.flow.Flow


@Dao
interface SetorDao {

    @Insert
    suspend fun insert( setor: Setor)

    @Query("SELECT * FROM setores ORDER BY nome ASC")
    fun getAllSetores(): Flow<List<Setor>>

    @Delete
    suspend fun delete(setor: Setor)

    @Update
    suspend fun update(setor: Setor) // ✅ NOVA FUNÇÃO

    @Query("SELECT * FROM setores WHERE id = :setorId")
    fun getSetorById(setorId: Long): Flow<Setor?>

}