package com.luanferreira.celestepass.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.luanferreira.celestepass.data.TimeSelecao
import com.luanferreira.celestepass.data.TimesPredefinidos
import com.luanferreira.celestepass.data.model.Jogo
import com.luanferreira.celestepass.data.repository.CelestePassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AdicionarJogoViewModel @Inject constructor(
    private val repository: CelestePassRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val jogoId: Long = savedStateHandle.get<Long>("jogoId") ?: -1L

    val jogoParaEdicao: LiveData<Jogo?> = if (jogoId != -1L) {
        repository.getJogoPorId(jogoId).asLiveData()
    } else {
        MutableLiveData(null)
    }

    private val _eventoSalvo = MutableLiveData<Boolean>()
    val eventoSalvo: LiveData<Boolean> get() = _eventoSalvo

    // --- Lógica da API e Lista de Times ---
    private val _listaTimesSelecao = MutableLiveData<List<TimeSelecao>>()
    val listaTimesSelecao: LiveData<List<TimeSelecao>> get() = _listaTimesSelecao

    private val _escudoUrlResult = MutableLiveData<Result<String?>>()
    val escudoUrlResult: LiveData<Result<String?>> get() = _escudoUrlResult

    private val _isLoadingEscudo = MutableLiveData<Boolean>()
    val isLoadingEscudo: LiveData<Boolean> get() = _isLoadingEscudo

    init {
        carregarTimesPredefinidos()
    }

    private fun carregarTimesPredefinidos() {
        _listaTimesSelecao.value = TimesPredefinidos.listaDeTimes
    }

    fun buscarEscudoAdversario(timeSelecionado: TimeSelecao) {
        _isLoadingEscudo.value = true
        _escudoUrlResult.value = null
        viewModelScope.launch {
            try {
                val response = repository.getEscudoDoTimePelaApi(timeSelecionado.idApi)
                if (response.isSuccessful) {
                    _escudoUrlResult.postValue(Result.success(response.body()?.crestUrl))
                } else {
                    _escudoUrlResult.postValue(Result.failure(Exception("Erro API: ${response.code()}")))
                }
            } catch (e: Exception) {
                _escudoUrlResult.postValue(Result.failure(e))
            }
            _isLoadingEscudo.postValue(false)
        }
    }

    fun salvarJogo(adversarioNome: String, adversarioEscudoUrl: String?, data: Date) {
        viewModelScope.launch {
            if (jogoId == -1L) {
                val novoJogo = Jogo(adversarioNome = adversarioNome, adversarioEscudoUrl = adversarioEscudoUrl, data = data)
                repository.insertJogo(novoJogo)
            } else {
                val jogoAtualizado = Jogo(id = jogoId, adversarioNome = adversarioNome, adversarioEscudoUrl = adversarioEscudoUrl, data = data)
                repository.updateJogo(jogoAtualizado)
            }
            _eventoSalvo.postValue(true)
        }
    }

    fun onEventoSalvoCompleto() {
        _eventoSalvo.value = false
    }
}