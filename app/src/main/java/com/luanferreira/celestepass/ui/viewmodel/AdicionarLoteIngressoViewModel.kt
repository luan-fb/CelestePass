package com.luanferreira.celestepass.ui.viewmodel

import androidx.lifecycle.*
import com.luanferreira.celestepass.data.model.Ingresso
import com.luanferreira.celestepass.data.model.Setor
import com.luanferreira.celestepass.data.repository.CelestePassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdicionarLoteIngressoViewModel @Inject constructor(
    private val repository: CelestePassRepository
) : ViewModel() {

    val todosOsSetores: LiveData<List<Setor>> = repository.getAllSetores().asLiveData()

    private val _loteSalvoEvento = MutableLiveData<Boolean>()
    val loteSalvoEvento: LiveData<Boolean> get() = _loteSalvoEvento

    fun salvarLoteDeIngressos(jogoId: Long, setorId: Long, quantidade: Int, valorCompra: Double) {
        val novoLote = Ingresso(
            jogoId = jogoId,
            setorId = setorId,
            quantidade = quantidade,
            valorCompra = valorCompra
        )
        viewModelScope.launch {
            repository.insertIngresso(novoLote)
            _loteSalvoEvento.postValue(true)
        }
    }

    fun onLoteSalvoEventoCompleto() {
        _loteSalvoEvento.value = false
    }
}
