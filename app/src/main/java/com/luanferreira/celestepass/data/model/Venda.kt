package com.luanferreira.celestepass.data.model


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "vendas",
    foreignKeys = [
        ForeignKey(
            entity = Ingresso::class,
            parentColumns = ["id"],
            childColumns = ["ingressoId"]
        ),
        ForeignKey(
            entity = Cliente::class,
            parentColumns = ["id"],
            childColumns = ["clienteId"]
        )
    ]
)
data class Venda (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val ingressoId: Long,
    val clienteId: Long,
    val quantidadeVendida: Int,
    val valorVendaUnitario: Double,
    val dataVenda: Date = Date(),
    var entregue: Boolean = false) {
}