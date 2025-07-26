package com.luanferreira.celestepass.ui.viewmodel

import android.widget.Toast
import androidx.lifecycle.*
import com.luanferreira.celestepass.data.model.Ingresso
import com.luanferreira.celestepass.data.model.Jogo
import com.luanferreira.celestepass.data.model.Venda
import com.luanferreira.celestepass.data.repository.CelestePassRepository
import com.luanferreira.celestepass.ui.adapter.IngressoComSetor
import com.luanferreira.celestepass.ui.adapter.VendaDetalhada
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

data class ResumoFinanceiroJogo(
    val investido: Double = 0.0,
    val vendido: Double = 0.0,
    val lucro: Double = 0.0
)

@HiltViewModel
class DetalhesJogoViewModel @Inject constructor(
    private val repository: CelestePassRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val jogoId: Long = savedStateHandle.get<Long>("jogoId")!!

    private val _eventoVendaNaoRemovida = MutableLiveData<Int>() // Posição na lista

    val eventoVendaNaoRemovida: LiveData<Int> = _eventoVendaNaoRemovida

    val jogo: LiveData<Jogo?> = repository.getJogoPorId(jogoId).asLiveData()

    private val _errorEvent = MutableLiveData<String?>()
    val errorEvent: LiveData<String?> get() = _errorEvent

    val ingressosComprados: LiveData<List<Ingresso>> =
        repository.getIngressosCompradosDoJogo(jogoId).asLiveData()

    val ingressosComSetor: LiveData<List<IngressoComSetor>> =
        repository.getIngressosCompradosDoJogo(jogoId)
            .combine(repository.getAllSetores()) { ingressos, setores ->
                ingressos.map { ingresso ->
                    IngressoComSetor(
                        ingresso = ingresso,
                        setor = setores.find { it.id == ingresso.setorId }
                    )
                }
            }.asLiveData()

    val vendasDetalhadas: LiveData<List<VendaDetalhada>> =
        repository.getVendasDoJogo(jogoId)
            .combine(repository.getIngressosCompradosDoJogo(jogoId)) { vendas, ingressos ->
                Pair(vendas, ingressos)
            }.combine(repository.getClientes()) { (vendas, ingressos), clientes ->
                Triple(vendas, ingressos, clientes)
            }.combine(repository.getAllSetores()) { (vendas, ingressos, clientes), setores ->
                vendas.map { venda ->
                    val ingressoAssociado = ingressos.find { it.id == venda.ingressoId }
                    VendaDetalhada(
                        venda = venda,
                        cliente = clientes.find { it.id == venda.clienteId },
                        ingresso = ingressoAssociado,
                        setor = setores.find { it.id == ingressoAssociado?.setorId }
                    )
                }
            }.asLiveData()


    val resumoFinanceiro: LiveData<ResumoFinanceiroJogo> =
        MediatorLiveData<ResumoFinanceiroJogo>().apply {
            var currentIngressos: List<Ingresso>? = null
            var currentVendas: List<VendaDetalhada>? = null

            fun update() {
                val ingressos = currentIngressos
                val vendas = currentVendas
                if (ingressos != null && vendas != null) {
                    val investido = ingressos.sumOf { it.valorCompra * it.quantidade }
                    val vendido =
                        vendas.sumOf { it.venda.valorVendaUnitario * it.venda.quantidadeVendida }
                    val custoDasVendas = vendas.sumOf {
                        (it.ingresso?.valorCompra ?: 0.0) * it.venda.quantidadeVendida
                    }
                    val lucro = vendido - custoDasVendas
                    value = ResumoFinanceiroJogo(
                        investido = investido,
                        vendido = vendido,
                        lucro = lucro
                    )
                }
            }

            addSource(ingressosComprados) { ingressos ->
                currentIngressos = ingressos
                update()
            }

            addSource(vendasDetalhadas) { vendas ->
                currentVendas = vendas
                update()
            }
        }

    private val _jogoDeletadoEvento = MutableLiveData<Boolean>()
    val jogoDeletadoEvento: LiveData<Boolean> get() = _jogoDeletadoEvento


    fun deletarJogoAtual() {
        jogo.value?.let { jogoParaDeletar ->
            viewModelScope.launch {
                // ✅ VERIFICAÇÃO: Checa se o jogo tem ingressos antes de deletar
                if (repository.getIngressosCountForJogo(jogoParaDeletar.id) > 0) {
                    _errorEvent.postValue("Este jogo não pode ser deletado pois possui lotes de ingressos cadastrados. Remova os ingressos primeiro.")
                } else {
                    // Se não houver ingressos, procede com a deleção
                    repository.deleteJogo(jogoParaDeletar)
                    _jogoDeletadoEvento.postValue(true)
                }
            }
        }
    }

    fun deleteVenda(vendaDetalhada: VendaDetalhada,posicao: Int) {
        val vendaParaDeletar = vendaDetalhada.venda
        val ingressoDoLote = vendaDetalhada.ingresso

        if (ingressoDoLote == null) return
        if (vendaParaDeletar.entregue) {
            _eventoVendaNaoRemovida.postValue(posicao)
            return
        }

        val loteAtualizado = ingressoDoLote.copy(
            quantidadeVendida = ingressoDoLote.quantidadeVendida - vendaParaDeletar.quantidadeVendida
        )
        viewModelScope.launch {
            repository.deleteVenda(vendaParaDeletar, loteAtualizado)
        }
    }

    fun onJogoDeletadoEventoCompleto() {
        _jogoDeletadoEvento.value = false
    }

    fun formatarMoeda(valor: Double): String {
        val formatoMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        return formatoMoeda.format(valor)
    }


    fun marcarVendaComoEntregue(venda: Venda) {
        viewModelScope.launch {
            val vendaAtualizada = venda.copy(entregue = true)
            repository.marcarVendaComoEntregue(vendaAtualizada)
        }
    }

    fun desmarcarVendaComoEntregue(venda: Venda) {
        viewModelScope.launch {
            val vendaAtualizada = venda.copy(entregue = false)
            repository.marcarVendaComoEntregue(vendaAtualizada) // Reutilizamos a mesma função do repositório
        }
    }


    fun deleteIngresso(ingresso: Ingresso) {
        viewModelScope.launch {
            repository.deleteIngresso(ingresso)
        }
    }

    fun onErrorEventConsumed() {
        _errorEvent.value = null
    }

}
