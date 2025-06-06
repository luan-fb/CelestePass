package com.luanferreira.celestepass.ui.view.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.luanferreira.celestepass.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EntregasJogoFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_tab_placeholder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Lógica para configurar RecyclerView para entregas pendentes.
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

