package com.luanferreira.celestepass.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetalhesJogoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
        setupViewPager()
        observeViewModel()

        binding.fabDetalhesJogo.setOnClickListener {
            mostrarDialogoDeAcoes()
        }
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.detalhes_jogo_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_edit -> {
                        val action = DetalhesJogoFragmentDirections.actionDetalhesJogoFragmentToAddGameFragment(args.jogoId)
                        findNavController().navigate(action)
                        true
                    }
                    R.id.action_delete -> {
                        mostrarDialogoDelecao()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
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
                val formatadorData = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                binding.textViewNomeJogoDetalhes.text = "Cruzeiro vs. ${it.adversarioNome} - ${formatadorData.format(it.data)}"

                binding.imageViewAdversarioLogo.load(it.adversarioEscudoUrl) {
                    placeholder(R.drawable.ic_shield)
                    error(R.drawable.ic_broken_image)
                }
            }
        }

        viewModel.resumoFinanceiro.observe(viewLifecycleOwner) { resumo ->
            resumo?.let {
                // Card Investido
                binding.cardInvestido.textViewCardTitle.text = "Investido"
                binding.cardInvestido.imageViewCardIcon.setImageResource(R.drawable.ic_investment)
                binding.cardInvestido.textViewCardValue.text = viewModel.formatarMoeda(it.investido)

                // Card Vendido
                binding.cardVendido.textViewCardTitle.text = "Vendido"
                binding.cardVendido.imageViewCardIcon.setImageResource(R.drawable.ic_revenue)
                binding.cardVendido.textViewCardValue.text = viewModel.formatarMoeda(it.vendido)

                // Card Lucro
                binding.cardLucro.textViewCardTitle.text = "Lucro"
                binding.cardLucro.imageViewCardIcon.setImageResource(R.drawable.ic_profit)
                binding.cardLucro.textViewCardValue.text = viewModel.formatarMoeda(it.lucro)
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
                AlertDialog.Builder(requireContext())
                    .setTitle("Ação não permitida")
                    .setMessage(it)
                    .setPositiveButton("Ok", null)
                    .show()
                viewModel.onErrorEventConsumed()
            }
        }
    }

    private fun mostrarDialogoDeAcoes()
    {
        val opcoes = arrayOf("Adicionar Lote de Ingressos", "Registar Venda")
        AlertDialog.Builder(requireContext())
            .setTitle("O que deseja fazer?")
            .setItems(opcoes) { dialog, which ->
                when (which) {
                    0 -> {
                        val action = DetalhesJogoFragmentDirections.actionDetalhesJogoFragmentToAddTicketLotFragment(args.jogoId)
                        findNavController().navigate(action)
                    }
                    1 -> {
                        val action = DetalhesJogoFragmentDirections.actionDetalhesJogoFragmentToRegistarVendaFragment(args.jogoId)
                        findNavController().navigate(action)
                    }
                }
                dialog.dismiss()
            }
            .show()
    }

    private fun mostrarDialogoDelecao()
    {
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

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }
}
