package com.luanferreira.celestepass.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.luanferreira.celestepass.databinding.FragmentDetalhesJogoBinding
import com.luanferreira.celestepass.ui.adapter.DetalhesJogoPagerAdapter
import com.luanferreira.celestepass.ui.viewmodel.DetalhesJogoViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class DetalhesJogoFragment : Fragment() {

    private var _binding: FragmentDetalhesJogoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetalhesJogoViewModel by viewModels()
    private val args: DetalhesJogoFragmentArgs by navArgs() // Para pegar o jogoId

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalhesJogoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // O jogoId é acessível via args.jogoId
        // Log.d("DetalhesJogoFragment", "ID do Jogo recebido: ${args.jogoId}")

        setupViewPager()
        observeViewModel()

        // Configurar o FAB (botão +) para futuras ações (ex: adicionar ingresso, registrar venda)
        binding.fabDetalhesJogo.setOnClickListener {
            // Implementar ações do FAB (ex: abrir um menu ou navegar para outra tela)
        }
    }

    private fun setupViewPager() {
        binding.viewPagerDetalhesJogo.adapter = DetalhesJogoPagerAdapter(this, args.jogoId)
        TabLayoutMediator(binding.tabLayoutDetalhesJogo, binding.viewPagerDetalhesJogo) { tab, position ->
            tab.text = when (position) {
                0 -> "Vendas"
                1 -> "Ingressos" // Ingressos comprados
                2 -> "Entregas"  // Entregas Pendentes
                else -> null
            }
        }.attach()
    }

    private fun observeViewModel() {
        viewModel.jogo.observe(viewLifecycleOwner) { jogo ->
            jogo?.let {
                val formatadorData = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("pt", "BR"))
                binding.textViewNomeJogoDetalhes.text = "Cruzeiro vs. ${it.adversarioNome}"
                binding.textViewDataJogoDetalhes.text = formatadorData.format(it.data)
                // Atualizar o título da ActionBar (opcional, pois a CollapsingToolbar já mostra)
                // (requireActivity() as AppCompatActivity).supportActionBar?.title = "Cruzeiro vs. ${it.adversarioNome}"
            }
        }

        viewModel.resumoFinanceiro.observe(viewLifecycleOwner) { resumo ->
            resumo?.let {
                binding.textViewInvestidoJogo.text = viewModel.formatarMoeda(it.investido)
                binding.textViewVendidoJogo.text = viewModel.formatarMoeda(it.vendido)
                binding.textViewLucroJogo.text = viewModel.formatarMoeda(it.lucro)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}