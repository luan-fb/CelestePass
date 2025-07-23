package com.luanferreira.celestepass.ui.adapter

import com.luanferreira.celestepass.data.model.*

// Data class para combinar todos os dados necessários para exibir uma venda
data class VendaDetalhada(
    val venda: Venda,
    val cliente: Cliente?,
    val ingresso: Ingresso?,
    val setor: Setor?
)
