package com.luanferreira.celestepass.ui.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.luanferreira.celestepass.R
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
    private val args: DetalhesJogoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalhesJogoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
        observeViewModel()


        binding.buttonDeletarJogo.setOnClickListener {
            mostrarDialogoDelecao()
        }

        binding.fabDetalhesJogo.setOnClickListener {
            mostrarDialogoDeAcoes()
        }



    }

    // A função para mostrar o diálogo de confirmação permanece a mesma
    private fun mostrarDialogoDelecao() {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmar Exclusão")
            .setMessage("Tem certeza de que deseja deletar este jogo? Esta ação não pode ser desfeita.")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Deletar") { dialog, _ ->
                viewModel.deletarJogoAtual()
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun mostrarDialogoDeAcoes() {
        val opcoes = arrayOf("Adicionar Lote de Ingressos", "Registar Venda")
        AlertDialog.Builder(requireContext())
            .setTitle("O que deseja fazer?")
            .setItems(opcoes) { dialog, which ->
                when (which) {
                    0 -> { // Adicionar Lote de Ingressos
                        val action = DetalhesJogoFragmentDirections.actionDetalhesJogoFragmentToAddTicketLotFragment(args.jogoId)
                        findNavController().navigate(action)
                    }
                    1 -> { // Registar Venda
                        val action = DetalhesJogoFragmentDirections.actionDetalhesJogoFragmentToRegistarVendaFragment(args.jogoId)
                        findNavController().navigate(action)
                    }
                }
                dialog.dismiss()
            }
            .show()
    }


    private fun setupViewPager() {
        binding.viewPagerDetalhesJogo.adapter = DetalhesJogoPagerAdapter(this, args.jogoId)
        TabLayoutMediator(binding.tabLayoutDetalhesJogo, binding.viewPagerDetalhesJogo) { tab, position ->
            tab.text = when (position) {
                0 -> "Vendas"
                1 -> "Ingressos"
                2 -> "Entregas"
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
            }
        }

        viewModel.resumoFinanceiro.observe(viewLifecycleOwner) { resumo ->
            resumo?.let {
                binding.textViewInvestidoJogo.text = viewModel.formatarMoeda(it.investido)
                binding.textViewVendidoJogo.text = viewModel.formatarMoeda(it.vendido)
                binding.textViewLucroJogo.text = viewModel.formatarMoeda(it.lucro)
            }
        }

        viewModel.jogoDeletadoEvento.observe(viewLifecycleOwner) { foiDeletado ->
            if (foiDeletado == true) {
                Toast.makeText(context, "Jogo deletado com sucesso!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
                viewModel.onJogoDeletadoEventoCompleto()
            }
        }

        viewModel.errorEvent.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                // Mostra um diálogo de alerta com a mensagem de erro
                AlertDialog.Builder(requireContext())
                    .setTitle("Ação não permitida")
                    .setMessage(it)
                    .setPositiveButton("Ok", null)
                    .show()
                // Avisa o ViewModel que a mensagem já foi mostrada
                viewModel.onErrorEventConsumed()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
