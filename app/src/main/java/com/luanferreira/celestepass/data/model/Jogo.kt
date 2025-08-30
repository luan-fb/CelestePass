package com.luanferreira.celestepass.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "jogos")
data class Jogo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val adversarioNome: String,
    val adversarioEscudoUrl: String?,
    val data: Date
)
