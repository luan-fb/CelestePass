package com.luanferreira.celestepass.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ingressos",
    foreignKeys = [
        ForeignKey(
            entity = Jogo::class,
            parentColumns = ["id"],
            childColumns = ["jogoId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = Setor::class,
            parentColumns = ["id"],
            childColumns = ["setorId"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)

data class Ingresso(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val jogoId: Long,
    val setorId: Long,
    val quantidade: Int,
    val valorCompra: Double,
    val quantidadeVendida: Int = 0
)