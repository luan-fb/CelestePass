package com.luanferreira.celestepass.ui.viewmodel

import androidx.lifecycle.*
import com.luanferreira.celestepass.data.model.Cliente
import com.luanferreira.celestepass.data.model.Setor
import com.luanferreira.celestepass.data.repository.CelestePassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagementViewModel @Inject constructor(
    private val repository: CelestePassRepository
) : ViewModel() {
    val allClientes: LiveData<List<Cliente>> = repository.getClientes().asLiveData()
    val allSetores: LiveData<List<Setor>> = repository.getAllSetores().asLiveData()
    private val _errorEvent = MutableLiveData<String?>()
    val errorEvent: LiveData<String?> get() = _errorEvent

    fun deleteCliente(cliente: Cliente) = viewModelScope.launch {
        val vendasDoCliente = repository.getVendasDoCliente(cliente.id).first()
        if (vendasDoCliente.isNotEmpty()) {
            _errorEvent.postValue("Este cliente não pode ser deletado pois possui vendas associadas.")
        } else {
            repository.deleteCliente(cliente)
        }
    }
    fun deleteSetor(setor: Setor) = viewModelScope.launch {
        // Futuramente, podemos adicionar uma verificação similar para setores
        repository.deleteSetor(setor)
    }
    fun onErrorEventConsumed() {
        _errorEvent.value = null
    }
}