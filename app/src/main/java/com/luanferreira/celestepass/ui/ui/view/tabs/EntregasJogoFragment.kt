package com.luanferreira.celestepass.ui.view.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.luanferreira.celestepass.databinding.FragmentEntregasJogoBinding
import com.luanferreira.celestepass.ui.adapter.EntregaAdapter
import com.luanferreira.celestepass.ui.viewmodel.DetalhesJogoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EntregasJogoFragment : Fragment() {

    private var _binding: FragmentEntregasJogoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetalhesJogoViewModel by viewModels({ requireParentFragment() })
    private lateinit var entregaAdapter: EntregaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEntregasJogoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        entregaAdapter = EntregaAdapter { vendaDetalhada, isEntregue ->
            if (isEntregue) {
                // Se já está entregue, a ação é desmarcar
                viewModel.desmarcarVendaComoEntregue(vendaDetalhada.venda)
            } else {
                // Se está pendente, a ação é marcar como entregue
                viewModel.marcarVendaComoEntregue(vendaDetalhada.venda)
            }
        }
        binding.recyclerViewEntregas.apply {
            adapter = entregaAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeViewModel() {
        viewModel.vendasDetalhadas.observe(viewLifecycleOwner) { listaCompletaDeVendas ->
            entregaAdapter.submitList(listaCompletaDeVendas)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(jogoId: Long) =
            EntregasJogoFragment().apply {
                arguments = Bundle().apply {
                    putLong("jogo_id", jogoId)
                }
            }
    }
}
