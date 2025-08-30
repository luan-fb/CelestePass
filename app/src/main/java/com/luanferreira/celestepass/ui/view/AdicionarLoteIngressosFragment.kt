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
import androidx.navigation.fragment.navArgs
import com.luanferreira.celestepass.data.model.Setor
import com.luanferreira.celestepass.databinding.FragmentAddTicketLotBinding
import com.luanferreira.celestepass.ui.viewmodel.AdicionarLoteIngressoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdicionarLoteIngressosFragment : Fragment() {

    private var _binding: FragmentAddTicketLotBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdicionarLoteIngressoViewModel by viewModels()
    private val args: AddTicketLotFragmentArgs by navArgs()

    private var setorSelecionado: Setor? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTicketLotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSetorAutoComplete()
        observeViewModel()

        binding.buttonSalvarLote.setOnClickListener {
            salvarLote()
        }
    }

    private fun setupSetorAutoComplete() {
        viewModel.todosOsSetores.observe(viewLifecycleOwner) { setores ->
            val nomesDosSetores = setores.map { it.nome }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                nomesDosSetores
            )
            (binding.inputLayoutSetor.editText as? AutoCompleteTextView)?.setAdapter(adapter)
            (binding.inputLayoutSetor.editText as? AutoCompleteTextView)?.setOnItemClickListener { _, _, position, _ ->
                val nomeSelecionado = adapter.getItem(position)
                setorSelecionado = setores.find { it.nome == nomeSelecionado }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.loteSalvoEvento.observe(viewLifecycleOwner) { foiSalvo ->
            if (foiSalvo == true) {
                Toast.makeText(context, "Lote de ingressos salvo!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
                viewModel.onLoteSalvoEventoCompleto()
            }
        }
    }

    private fun salvarLote() {
        val quantidadeStr = binding.editTextQuantidade.text.toString()
        val valorCompraStr = binding.editTextValorCompra.text.toString()

        if (setorSelecionado == null) {
            Toast.makeText(context, "Por favor, selecione um setor.", Toast.LENGTH_SHORT).show()
            return
        }
        if (quantidadeStr.isBlank() || valorCompraStr.isBlank()) {
            Toast.makeText(context, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

        val quantidade = quantidadeStr.toIntOrNull() ?: 0
        val valorCompra = valorCompraStr.toDoubleOrNull() ?: 0.0

        if (quantidade <= 0 || valorCompra <= 0.0) {
            Toast.makeText(context, "Quantidade e valor devem ser maiores que zero.", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.salvarLoteDeIngressos(args.jogoId, setorSelecionado!!.id, quantidade, valorCompra)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
