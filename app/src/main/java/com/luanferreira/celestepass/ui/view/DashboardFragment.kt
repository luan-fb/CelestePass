package com.luanferreira.celestepass.ui.view

import android.os.Bundle
import android.util.Log // Adicione para teste
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.luanferreira.celestepass.R
import com.luanferreira.celestepass.databinding.FragmentDashboardBinding
import com.luanferreira.celestepass.ui.adapter.JogoAdapter
import com.luanferreira.celestepass.ui.viewmodel.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels()
    private val adapter = JogoAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        binding.fabAdicionarJogo.setOnClickListener {
            Log.d("DashboardFragment", "FAB (botão +) clicado!") // Log de teste
            // ✅ AÇÃO DE NAVEGAÇÃO CORRETA AQUI ✅
            findNavController().navigate(R.id.action_dashboardFragment_to_addGameFragment)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewJogos.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewJogos.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.todosOsJogos.observe(viewLifecycleOwner) { jogos ->
            jogos?.let {
                adapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}