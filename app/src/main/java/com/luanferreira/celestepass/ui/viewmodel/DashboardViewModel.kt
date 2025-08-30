package com.luanferreira.celestepass.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.luanferreira.celestepass.data.model.Ingresso
import com.luanferreira.celestepass.data.model.Jogo
import com.luanferreira.celestepass.data.model.Venda
import com.luanferreira.celestepass.data.repository.CelestePassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: CelestePassRepository
) : ViewModel() {

    val todosOsJogos: LiveData<List<Jogo>> = repository.getAllJogos().asLiveData()

    // Combina o fluxo de todas as vendas com o fluxo de todos os ingressos
    private val allVendasAndIngressos = repository.getAllVendas().combine(repository.getAllIngressos()) { vendas, ingressos ->
        Pair(vendas, ingressos)
    }

    // Calcula o lucro do mês sempre que a lista de vendas ou ingressos mudar
    val lucroDoMes: LiveData<String> = allVendasAndIngressos.map { (vendas, ingressos) ->
        calculateProfitForPeriod(vendas, ingressos, Period.CURRENT_MONTH)
    }.asLiveData().map { formatarMoeda(it) }

    // Calcula o lucro do ano sempre que a lista de vendas ou ingressos mudar
    val lucroDoAno: LiveData<String> = allVendasAndIngressos.map { (vendas, ingressos) ->
        calculateProfitForPeriod(vendas, ingressos, Period.CURRENT_YEAR)
    }.asLiveData().map { formatarMoeda(it) }

    private enum class Period { CURRENT_MONTH, CURRENT_YEAR }

    private fun calculateProfitForPeriod(vendas: List<Venda>, ingressos: List<Ingresso>, period: Period): Double {
        val calendario = Calendar.getInstance()
        val mesAtual = if (period == Period.CURRENT_MONTH) calendario.get(Calendar.MONTH) else -1
        val anoAtual = calendario.get(Calendar.YEAR)

        // 1. Filtra as vendas para o período desejado (mês ou ano)
        val vendasFiltradas = vendas.filter { venda ->
            val calendarioVenda = Calendar.getInstance().apply { time = venda.dataVenda }
            val anoVenda = calendarioVenda.get(Calendar.YEAR)
            val mesVenda = calendarioVenda.get(Calendar.MONTH)

            if (period == Period.CURRENT_YEAR) {
                anoVenda == anoAtual
            } else { // CURRENT_MONTH
                anoVenda == anoAtual && mesVenda == mesAtual
            }
        }
        // 2. Calcula o lucro total para as vendas filtradas
        var lucroTotal = 0.0
        for (venda in vendasFiltradas) {
            // Encontra o lote de ingresso original para saber o custo
            val ingressoDoLote = ingressos.find { it.id == venda.ingressoId }
            val custo = (ingressoDoLote?.valorCompra ?: 0.0) * venda.quantidadeVendida
            val receita = venda.valorVendaUnitario * venda.quantidadeVendida
            lucroTotal += (receita - custo)
        }
        return lucroTotal
    }
    private fun formatarMoeda(valor: Double): String {
        val formatoMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        return formatoMoeda.format(valor)
    }
}