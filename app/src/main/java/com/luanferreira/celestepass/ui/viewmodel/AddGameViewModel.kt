package com.luanferreira.celestepass.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luanferreira.celestepass.data.TimeSelecao
import com.luanferreira.celestepass.data.TimesPredefinidos
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

    private val _listaTimesSelecao = MutableLiveData<List<TimeSelecao>>()
    val listaTimesSelecao: LiveData<List<TimeSelecao>> get() = _listaTimesSelecao

    private val _escudoUrlResult = MutableLiveData<Result<String?>>()
    val escudoUrlResult: LiveData<Result<String?>> get() = _escudoUrlResult

    private val _isLoadingEscudo = MutableLiveData<Boolean>()
    val isLoadingEscudo: LiveData<Boolean> get() = _isLoadingEscudo

    // Para notificar a UI que o jogo foi salvo e pode navegar de volta
    private val _jogoSalvoEvento = MutableLiveData<Boolean>()
    val jogoSalvoEvento: LiveData<Boolean> get() = _jogoSalvoEvento

    init {
        carregarTimesPredefinidos()
    }

    private fun carregarTimesPredefinidos() {
        _listaTimesSelecao.value = TimesPredefinidos.listaDeTimes
    }

    fun buscarEscudoAdversario(timeSelecionado: TimeSelecao) {
        _isLoadingEscudo.value = true
        _escudoUrlResult.value = null // Limpa resultado anterior
        viewModelScope.launch {
            try {
                Log.d("AddGameViewModel", "Buscando escudo para ID: ${timeSelecionado.idApi}")
                val response = repository.getEscudoDoTimePelaApi(timeSelecionado.idApi)
                if (response.isSuccessful) {
                    val crestUrl = response.body()?.crestUrl
                    Log.d("AddGameViewModel", "API Sucesso. URL Escudo: $crestUrl")
                    _escudoUrlResult.postValue(Result.success(crestUrl))
                } else {
                    Log.e("AddGameViewModel", "API Erro. Código: ${response.code()}, Mensagem: ${response.message()}")
                    _escudoUrlResult.postValue(Result.failure(Exception("Erro API: ${response.code()}")))
                }
            } catch (e: Exception) {
                Log.e("AddGameViewModel", "Exceção ao buscar escudo: ${e.message}", e)
                _escudoUrlResult.postValue(Result.failure(e))
            }
            _isLoadingEscudo.postValue(false)
        }
    }

    fun salvarJogo(nomeAdversario: String, escudoUrl: String?, data: Date) {
        if (nomeAdversario.isBlank()) {
            // Idealmente, emitir um evento de erro para a UI
            Log.w("AddGameViewModel", "Tentativa de salvar jogo com nome de adversário em branco.")
            return
        }
        val novoJogo = Jogo(
            adversarioNome = nomeAdversario,
            adversarioEscudoUrl = escudoUrl,
            data = data
        )
        viewModelScope.launch {
            Log.d("AddGameViewModel", "Salvando jogo: $novoJogo")
            repository.insertJogo(novoJogo)
            _jogoSalvoEvento.postValue(true) // Notifica que o jogo foi salvo
            Log.d("AddGameViewModel", "Jogo salvo no repositório.")
        }
    }

    fun onJogoSalvoEventoCompleto() {
        _jogoSalvoEvento.value = false // Reseta o evento
    }
}
