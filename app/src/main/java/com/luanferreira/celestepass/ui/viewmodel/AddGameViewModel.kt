package com.luanferreira.celestepass.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luanferreira.celestepass.data.model.Jogo
import com.luanferreira.celestepass.data.repository.CelestePassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddGameViewModel @Inject constructor(
    private val repository: CelestePassRepository
) : ViewModel() {

    fun salvarJogo(nomeAdversario: String, data: Date) {
        // Validações podem ser adicionadas aqui (ex: nome não pode ser vazio)
        if (nomeAdversario.isBlank()) {
            // Lidar com o erro (ex: emitir um LiveData de erro)
            return
        }

        val novoJogo = Jogo(adversario = nomeAdversario, data = data)
        viewModelScope.launch {
            repository.insertJogo(novoJogo)
        }
    }
}