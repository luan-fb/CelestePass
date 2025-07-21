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
class VendasJogoFragment : Fragment() {

    // ✅ CORREÇÃO: Usamos viewModels({ requireParentFragment() }) para partilhar
    // o ViewModel com o DetalhesJogoFragment (o pai).
    private val viewModel: DetalhesJogoViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Por agora, continuamos a usar o layout de placeholder.
        // Futuramente, aqui teremos um RecyclerView para as vendas.
        return inflater.inflate(R.layout.fragment_tab_placeholder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Aqui você pode observar os dados de vendas do viewModel
        // viewModel.vendasDoJogo.observe(viewLifecycleOwner) { vendas -> ... }
    }

    companion object {
        private const val ARG_JOGO_ID = "jogo_id"
        fun newInstance(jogoId: Long) =
            VendasJogoFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_JOGO_ID, jogoId)
                }
            }
    }
}
