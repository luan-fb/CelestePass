package com.luanferreira.celestepass.ui.viewmodel

import androidx.lifecycle.*
import com.luanferreira.celestepass.data.model.Cliente
import com.luanferreira.celestepass.data.repository.CelestePassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddClienteViewModel @Inject constructor(
    private val repository: CelestePassRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val clienteId: Long = savedStateHandle.get<Long>("clienteId") ?: -1L

    val clienteParaEdicao: LiveData<Cliente?> = if (clienteId != -1L) {
        repository.getClienteById(clienteId).asLiveData()
    } else {
        MutableLiveData(null)
    }

    // ✅ CORREÇÃO: Nomes consistentes para o evento
    private val _clienteSalvoEvento = MutableLiveData<Boolean>()
    val clienteSalvoEvento: LiveData<Boolean> get() = _clienteSalvoEvento

    fun salvarCliente(nome: String, cpf: String?, email: String?, telefone: String?, dataNascimento: Date?) {
        if (nome.isBlank()) return

        viewModelScope.launch {
            if (clienteId == -1L) {
                // MODO ADICIONAR
                val novoCliente = Cliente(nome = nome, cpf = cpf, email = email, telefone = telefone, dataNascimento = dataNascimento)
                repository.insertCliente(novoCliente)
            } else {
                // MODO EDITAR
                val clienteAtualizado = Cliente(id = clienteId, nome = nome, cpf = cpf, email = email, telefone = telefone, dataNascimento = dataNascimento)
                repository.updateCliente(clienteAtualizado)
            }
            _clienteSalvoEvento.postValue(true)
        }
    }

    // ✅ CORREÇÃO: Função de reset correspondente ao evento
    fun onClienteSalvoEventoCompleto() {
        _clienteSalvoEvento.value = false
    }
}

