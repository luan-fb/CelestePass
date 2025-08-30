package com.luanferreira.celestepass.ui.viewmodel

import androidx.lifecycle.*
import com.luanferreira.celestepass.data.model.Cliente
import com.luanferreira.celestepass.data.model.Venda
import com.luanferreira.celestepass.data.repository.CelestePassRepository
import com.luanferreira.celestepass.ui.adapter.IngressoComSetor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class RegistrarVendaViewModel @Inject constructor(
    private val repository: CelestePassRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val jogoId: Long = savedStateHandle.get<Long>("jogoId")!!

    val clientes: LiveData<List<Cliente>> = repository.getClientes().asLiveData()

    val lotesDisponiveis: LiveData<List<IngressoComSetor>> =
        repository.getIngressosCompradosDoJogo(jogoId)
            .combine(repository.getAllSetores()) { ingressos, setores ->
                ingressos
                    .filter { it.quantidade > it.quantidadeVendida } // Filtra apenas lotes com ingressos disponíveis
                    .map { ingresso ->
                        IngressoComSetor(
                            ingresso = ingresso,
                            setor = setores.find { it.id == ingresso.setorId }
                        )
                    }
            }.asLiveData()

    private val _vendaSalvaEvento = MutableLiveData<Boolean>()
    val vendaSalvaEvento: LiveData<Boolean> get() = _vendaSalvaEvento

    fun registarVenda(lote: IngressoComSetor, cliente: Cliente, qtdVendida: Int, valorVenda: Double) {
        // Validação
        if (qtdVendida <= 0 || valorVenda <= 0.0) return
        if (qtdVendida > (lote.ingresso.quantidade - lote.ingresso.quantidadeVendida)) return

        val novaVenda = Venda(
            ingressoId = lote.ingresso.id,
            clienteId = cliente.id,
            quantidadeVendida = qtdVendida,
            valorVendaUnitario = valorVenda,
            dataVenda = Date() // Data atual
        )

        val loteAtualizado = lote.ingresso.copy(
            quantidadeVendida = lote.ingresso.quantidadeVendida + qtdVendida
        )

        viewModelScope.launch {
            repository.registarVenda(novaVenda, loteAtualizado)
            _vendaSalvaEvento.postValue(true)
        }
    }

    fun onVendaSalvaEventoCompleto() {
        _vendaSalvaEvento.value = false
    }
}
