package com.luanferreira.celestepass.ui.view.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels // Mude o import
import com.luanferreira.celestepass.R
import com.luanferreira.celestepass.ui.viewmodel.DetalhesJogoViewModel // Importe o ViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EntregasJogoFragment : Fragment() {

    // ✅ CORREÇÃO: Usamos viewModels({ requireParentFragment() })
    private val viewModel: DetalhesJogoViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tab_placeholder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Lógica futura para a aba de entregas
    }

    companion object {
        private const val ARG_JOGO_ID = "jogo_id"
        fun newInstance(jogoId: Long) =
            EntregasJogoFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_JOGO_ID, jogoId)
                }
            }
    }
}