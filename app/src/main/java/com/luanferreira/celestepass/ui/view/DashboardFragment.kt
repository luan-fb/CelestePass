package com.luanferreira.celestepass.ui.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
            mostrarDialogoDeAcoesFab()
        }


        // Configurar listener para clique no item do jogo (para ir para detalhes)
        adapter.onItemClickListener = { jogo ->
            // ✅ NAVEGAÇÃO PARA DETALHES DO JOGO COM O ID ✅
            val action = DashboardFragmentDirections.actionDashboardFragmentToDetalhesJogoFragment(jogo.id)
            findNavController().navigate(action)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewJogos.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewJogos.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.todosOsJogos.observe(viewLifecycleOwner) { jogos ->
            jogos?.let {
                Log.d("DashboardFragment", "Lista de jogos recebida, tamanho: ${it.size}")
                adapter.submitList(it)
            }
        }

        viewModel.lucroDoMes.observe(viewLifecycleOwner) { lucroMes ->
            binding.textViewLucroMes.text = lucroMes
        }

        viewModel.lucroDoAno.observe(viewLifecycleOwner) { lucroAno ->
            binding.textViewLucroAno.text = lucroAno
        }
    }

    private fun mostrarDialogoDeAcoesFab() {
        val opcoes = arrayOf("Cadastrar Jogo", "Gerir Cadastros") // Opções atualizadas
        AlertDialog.Builder(requireContext())
            .setTitle("O que deseja fazer?")
            .setItems(opcoes) { dialog, which ->
                when (which) {
                    0 -> findNavController().navigate(R.id.action_dashboardFragment_to_addGameFragment)
                    1 -> findNavController().navigate(R.id.action_dashboardFragment_to_managementFragment)
                }
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}