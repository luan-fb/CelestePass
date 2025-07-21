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
    private val repository: CelestePassRepository
) : ViewModel() { // ✅ CORREÇÃO: Adicionado : ViewModel()
    private val _clienteSalvoEvento = MutableLiveData<Boolean>()
    val clienteSalvoEvento: LiveData<Boolean> get() = _clienteSalvoEvento

    fun salvarCliente(nome: String, cpf: String?, email: String?, telefone: String?, dataNascimento: Date?) {
        if (nome.isBlank()) return
        val novoCliente = Cliente(
            nome = nome,
            cpf = cpf,
            email = email,
            telefone = telefone,
            dataNascimento = dataNascimento
        )
        viewModelScope.launch {
            repository.insertCliente(novoCliente)
            _clienteSalvoEvento.postValue(true)
        }
    }

    fun onClienteSalvoEventoCompleto() { _clienteSalvoEvento.value = false }
}