package com.luanferreira.celestepass.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.luanferreira.celestepass.data.model.Cliente
import com.luanferreira.celestepass.databinding.FragmentRegistarVendaBinding
import com.luanferreira.celestepass.ui.adapter.IngressoComSetor
import com.luanferreira.celestepass.ui.viewmodel.RegistrarVendaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistarVendaFragment : Fragment() {

    private var _binding: FragmentRegistarVendaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegistrarVendaViewModel by viewModels()

    private var loteSelecionado: IngressoComSetor? = null
    private var clienteSelecionado: Cliente? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistarVendaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAutoCompleteFields()
        observeViewModel()

        binding.buttonSalvarVenda.setOnClickListener {
            salvarVenda()
        }
    }

    private fun setupAutoCompleteFields() {
        // Configura o AutoComplete para Lotes de Ingressos
        viewModel.lotesDisponiveis.observe(viewLifecycleOwner) { lotes ->
            val descricoesLotes = lotes.map {
                val disponiveis = it.ingresso.quantidade - it.ingresso.quantidadeVendida
                "${it.setor?.nome ?: ""} (${disponiveis} disp.)"
            }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, descricoesLotes)
            (binding.inputLayoutLoteIngresso.editText as? AutoCompleteTextView)?.setAdapter(adapter)
            (binding.inputLayoutLoteIngresso.editText as? AutoCompleteTextView)?.setOnItemClickListener { _, _, position, _ ->
                loteSelecionado = lotes[position]
            }
        }

        // Configura o AutoComplete para Clientes
        viewModel.clientes.observe(viewLifecycleOwner) { clientes ->
            val nomesClientes = clientes.map { it.nome }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, nomesClientes)
            (binding.inputLayoutCliente.editText as? AutoCompleteTextView)?.setAdapter(adapter)
            (binding.inputLayoutCliente.editText as? AutoCompleteTextView)?.setOnItemClickListener { _, _, position, _ ->
                clienteSelecionado = clientes[position]
            }
        }
    }

    private fun observeViewModel() {
        viewModel.vendaSalvaEvento.observe(viewLifecycleOwner) { foiSalva ->
            if (foiSalva == true) {
                Toast.makeText(context, "Venda registada com sucesso!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
                viewModel.onVendaSalvaEventoCompleto()
            }
        }
    }

    private fun salvarVenda() {
        val qtdStr = binding.editTextQuantidadeVenda.text.toString()
        val valorStr = binding.editTextValorVenda.text.toString()

        if (loteSelecionado == null || clienteSelecionado == null || qtdStr.isBlank() || valorStr.isBlank()) {
            Toast.makeText(context, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

        val quantidade = qtdStr.toIntOrNull() ?: 0
        val valor = valorStr.toDoubleOrNull() ?: 0.0
        val disponiveis = loteSelecionado!!.ingresso.quantidade - loteSelecionado!!.ingresso.quantidadeVendida

        if (quantidade > disponiveis) {
            Toast.makeText(context, "Quantidade indisponível. Apenas $disponiveis ingressos restantes neste lote.", Toast.LENGTH_LONG).show()
            return
        }

        viewModel.registarVenda(loteSelecionado!!, clienteSelecionado!!, quantidade, valor)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
