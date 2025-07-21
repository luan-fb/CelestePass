package com.luanferreira.celestepass.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "clientes")
data class Cliente(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String,
    val cpf: String?,
    val email: String?,
    val telefone : String?,
    val dataNascimento: Date?
)