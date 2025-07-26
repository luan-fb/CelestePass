package com.luanferreira.celestepass.ui.viewmodel

import androidx.lifecycle.*
import com.luanferreira.celestepass.data.model.Setor
import com.luanferreira.celestepass.data.repository.CelestePassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddSetorViewModel @Inject constructor(
    private val repository: CelestePassRepository,
    savedStateHandle: SavedStateHandle // Adiciona o SavedStateHandle
) : ViewModel() {

    // ✅ LÓGICA DE EDIÇÃO
    private val setorId: Long = savedStateHandle.get<Long>("setorId") ?: -1L

    val setorParaEdicao: LiveData<Setor?> = if (setorId != -1L) {
        repository.getSetorById(setorId).asLiveData()
    } else {
        MutableLiveData(null)
    }

    private val _setorSalvoEvento = MutableLiveData<Boolean>()
    val setorSalvoEvento: LiveData<Boolean> get() = _setorSalvoEvento

    fun salvarSetor(nomeSetor: String) {
        if (nomeSetor.isBlank()) return

        viewModelScope.launch {
            if (setorId == -1L) {
                // MODO ADICIONAR
                repository.insertSetor(Setor(nome = nomeSetor))
            } else {
                // MODO EDITAR
                repository.updateSetor(Setor(id = setorId, nome = nomeSetor))
            }
            _setorSalvoEvento.postValue(true)
        }
    }
    fun onSetorSalvoEventoCompleto() { _setorSalvoEvento.value = false }
}
