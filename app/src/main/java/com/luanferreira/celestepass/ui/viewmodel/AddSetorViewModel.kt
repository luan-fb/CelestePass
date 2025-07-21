package com.luanferreira.celestepass.ui.viewmodel

import androidx.lifecycle.*
import com.luanferreira.celestepass.data.model.Setor
import com.luanferreira.celestepass.data.repository.CelestePassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddSetorViewModel @Inject constructor(
    private val repository: CelestePassRepository
) : ViewModel() {
    private val _setorSalvoEvento = MutableLiveData<Boolean>()
    val setorSalvoEvento: LiveData<Boolean> get() = _setorSalvoEvento

    fun salvarSetor(nomeSetor: String) {
        if (nomeSetor.isBlank()) return
        viewModelScope.launch {
            repository.insertSetor(Setor(nome = nomeSetor))
            _setorSalvoEvento.postValue(true)
        }
    }
    fun onSetorSalvoEventoCompleto() { _setorSalvoEvento.value = false }
}
