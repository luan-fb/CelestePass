package com.luanferreira.celestepass.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.luanferreira.celestepass.data.model.Cliente
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteDao {
    @Insert
    suspend fun insert(cliente: Cliente)

    @Query("SELECT * FROM clientes ORDER BY nome ASC")
    fun getAllClientes(): Flow<List<Cliente>>

    @Delete
    suspend fun delete(cliente: Cliente)

    @Update
    suspend fun update(cliente: Cliente)

    @Query("SELECT * FROM clientes WHERE id = :clienteId")
    fun getClienteById(clienteId: Long): Flow<Cliente?>

}
