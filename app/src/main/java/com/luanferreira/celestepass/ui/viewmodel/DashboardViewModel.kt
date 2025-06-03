package com.luanferreira.celestepass.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.luanferreira.celestepass.data.repository.CelestePassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: CelestePassRepository
) : ViewModel() {

    val todosOsJogos = repository.getAllJogos().asLiveData()
}