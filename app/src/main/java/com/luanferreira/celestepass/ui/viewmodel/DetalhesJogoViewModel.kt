package com.luanferreira.celestepass.ui.viewmodel

import androidx.lifecycle.*
import com.luanferreira.celestepass.data.model.Ingresso
import com.luanferreira.celestepass.data.model.Jogo
import com.luanferreira.celestepass.data.model.Venda
import com.luanferreira.celestepass.data.repository.CelestePassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

// Data class para o resumo financeiro do jogo
data class ResumoFinanceiroJogo(
    val investido: Double = 0.0,
    val vendido: Double = 0.0,
    val lucro: Double = 0.0
)

@HiltViewModel
class DetalhesJogoViewModel @Inject constructor(
    private val repository: CelestePassRepository,
    private val savedStateHandle: SavedStateHandle // Para pegar o jogoId dos argumentos de navegação
) : ViewModel() {

    private val jogoId: Long = savedStateHandle.get<Long>("jogoId")!!

    val jogo: LiveData<Jogo?> = repository.getJogoPorId(jogoId).asLiveData()

    val ingressosComprados: LiveData<List<Ingresso>> = repository.getIngressosCompradosDoJogo(jogoId).asLiveData()
    val vendasDoJogo: LiveData<List<Venda>> = repository.getVendasDoJogo(jogoId).asLiveData()

    // LiveData para o resumo financeiro do jogo específico
    val resumoFinanceiro: LiveData<ResumoFinanceiroJogo> =
        MediatorLiveData<ResumoFinanceiroJogo>().apply {
            // Observa as vendas e os ingressos comprados para recalcular o resumo
            // Esta é uma lógica complexa que precisa ser bem pensada
            // Por ora, vamos simplificar ou deixar para implementar depois
            // Para calcular o investimento, precisamos dos valores de compra dos ingressos vendidos.
            // Para calcular o valor vendido, pegamos das vendas.

            // Exemplo muito simplificado e INCOMPLETO de cálculo
            // A forma correta envolveria buscar os ingressos de cada venda para pegar o valorCompra
            addSource(vendasDoJogo) { vendas ->
                val vendido = vendas.sumOf { it.valorVendaUnitario * it.quantidadeVendida }
                // O cálculo do investido aqui é um placeholder, pois precisaria dos dados dos ingressos
                val investidoPlaceholder = vendas.sumOf { 10.0 * it.quantidadeVendida } // VALOR DE COMPRA FICTÍCIO
                value = ResumoFinanceiroJogo(
                    investido = investidoPlaceholder,
                    vendido = vendido,
                    lucro = vendido - investidoPlaceholder
                )
            }
        }


    fun formatarMoeda(valor: Double): String {
        val formatoMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        return formatoMoeda.format(valor)
    }
}
