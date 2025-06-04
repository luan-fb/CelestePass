package com.luanferreira.celestepass.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "jogos")
data class Jogo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val adversarioNome: String,
    val adversarioEscudoUrl: String?, // Pode ser nulo se a API falhar ou não tiver escudo
    val data: Date
)
