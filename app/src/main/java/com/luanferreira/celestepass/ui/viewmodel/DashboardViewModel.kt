package com.luanferreira.celestepass.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.luanferreira.celestepass.data.model.Jogo
import com.luanferreira.celestepass.data.model.Venda // Importe Venda
import com.luanferreira.celestepass.data.repository.CelestePassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: CelestePassRepository
) : ViewModel() {

    val todosOsJogos: LiveData<List<Jogo>> = repository.getAllJogos().asLiveData()

    // LiveData para os resumos financeiros
    // A lógica de cálculo real será mais complexa
    val lucroDoMes: LiveData<String> = repository.getVendasDoMes().asLiveData().map { vendas ->
        formatarMoeda(calcularLucro(vendas, true)) // true para mês atual
    }

    val lucroDoAno: LiveData<String> = repository.getVendasDoAno().asLiveData().map { vendas ->
        formatarMoeda(calcularLucro(vendas, false)) // false para ano atual
    }

    private fun calcularLucro(vendas: List<Venda>, apenasMesAtual: Boolean): Double {
        var lucroTotal = 0.0
        val calendario = Calendar.getInstance()
        val mesAtual = if (apenasMesAtual) calendario.get(Calendar.MONTH) else -1 // -1 para não filtrar por mês
        val anoAtual = calendario.get(Calendar.YEAR)

        for (venda in vendas) {
            val calendarioVenda = Calendar.getInstance().apply { time = venda.dataVenda }
            val mesVenda = calendarioVenda.get(Calendar.MONTH)
            val anoVenda = calendarioVenda.get(Calendar.YEAR)

            if (anoVenda == anoAtual && (mesAtual == -1 || mesVenda == mesAtual)) {
                // AQUI VOCÊ PRECISARÁ ACESSAR O Ingresso RELACIONADO À VENDA
                // PARA OBTER O valorCompra. Isso exigirá uma query mais complexa
                // ou uma estrutura de dados que já traga essa informação.
                // Por simplicidade, vamos assumir um valor de compra fixo por enquanto.
                val valorCompraIngresso = 10.0 // EXEMPLO, SUBSTITUA PELA LÓGICA REAL
                lucroTotal += (venda.valorVendaUnitario * venda.quantidadeVendida) - (valorCompraIngresso * venda.quantidadeVendida)
            }
        }
        return lucroTotal
    }

    private fun formatarMoeda(valor: Double): String {
        val formatoMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        return formatoMoeda.format(valor)
    }
}
