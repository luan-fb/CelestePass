package com.luanferreira.celestepass.ui.view.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels // Mude o import
import androidx.recyclerview.widget.LinearLayoutManager
import com.luanferreira.celestepass.databinding.FragmentIngressosCompradosBinding
import com.luanferreira.celestepass.ui.adapter.IngressoAdapter
import com.luanferreira.celestepass.ui.viewmodel.DetalhesJogoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IngressosCompradosFragment : Fragment() {

    private var _binding: FragmentIngressosCompradosBinding? = null
    private val binding get() = _binding!!

    // ✅ CORREÇÃO: Usamos viewModels({ requireParentFragment() })
    private val viewModel: DetalhesJogoViewModel by viewModels({ requireParentFragment() })
    private val ingressoAdapter = IngressoAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngressosCompradosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewIngressosComprados.apply {
            adapter = ingressoAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeViewModel() {
        // Esta observação agora funcionará corretamente
        viewModel.ingressosComSetor.observe(viewLifecycleOwner) { listaIngressos ->
            ingressoAdapter.submitList(listaIngressos)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_JOGO_ID = "jogo_id"
        fun newInstance(jogoId: Long) =
            IngressosCompradosFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_JOGO_ID, jogoId)
                }
            }
    }
}