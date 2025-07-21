package com.luanferreira.celestepass.ui.viewmodel

import androidx.lifecycle.*
import com.luanferreira.celestepass.data.model.Cliente
import com.luanferreira.celestepass.data.model.Setor
import com.luanferreira.celestepass.data.repository.CelestePassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagementViewModel @Inject constructor(
    private val repository: CelestePassRepository
) : ViewModel() {
    val allClientes: LiveData<List<Cliente>> = repository.getClientes().asLiveData()
    val allSetores: LiveData<List<Setor>> = repository.getAllSetores().asLiveData()

    fun deleteCliente(cliente: Cliente) = viewModelScope.launch {
        repository.deleteCliente(cliente)
    }

    fun deleteSetor(setor: Setor) = viewModelScope.launch {
        repository.deleteSetor(setor)
    }
}
