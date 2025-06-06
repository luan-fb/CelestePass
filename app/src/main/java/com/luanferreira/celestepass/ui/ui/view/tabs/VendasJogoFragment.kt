package com.luanferreira.celestepass.ui.view.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.luanferreira.celestepass.R // Ou seu R específico do módulo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VendasJogoFragment : Fragment() {
    private var jogoId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            jogoId = it.getLong(ARG_JOGO_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar o layout para este fragmento (ex: fragment_vendas_jogo.xml)
        // Por enquanto, um layout simples com um TextView
        return inflater.inflate(R.layout.fragment_tab_placeholder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Configurar RecyclerView para mostrar vendas do jogo com ID: jogoId
        // (view.findViewById<TextView>(R.id.text_placeholder)).text = "Vendas do Jogo ID: $jogoId"
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