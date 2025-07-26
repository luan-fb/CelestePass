package com.luanferreira.celestepass.ui.viewmodel

import androidx.lifecycle.*
import com.luanferreira.celestepass.data.model.Cliente
import com.luanferreira.celestepass.data.repository.CelestePassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClienteDetalhesViewModel @Inject constructor(
    repository: CelestePassRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val clienteId: Long = savedStateHandle.get<Long>("clienteId")!!
    val cliente: LiveData<Cliente?> = repository.getClienteById(clienteId).asLiveData()
}