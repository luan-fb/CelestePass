package com.luanferreira.celestepass.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.datepicker.MaterialDatePicker
import com.luanferreira.celestepass.R
import com.luanferreira.celestepass.data.TimeSelecao
import com.luanferreira.celestepass.databinding.FragmentAdicionarJogoBinding
import com.luanferreira.celestepass.ui.viewmodel.AdicionarJogoViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AdicionarJogoFragment : Fragment() {

    private var _binding: FragmentAdicionarJogoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdicionarJogoViewModel by viewModels()
    private val args: AdicionarJogoFragmentArgs by navArgs()

    private var dataSelecionada: Date? = null
    private var timeAdversarioSelecionado: TimeSelecao? = null
    private var urlEscudoAdversarioAtual: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAdicionarJogoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        observeViewModel()

        if (args.jogoId != -1L) {
            (activity as? androidx.appcompat.app.AppCompatActivity)?.supportActionBar?.title = "Editar Jogo"
        }
    }

    private fun setupListeners() {

        viewModel.listaTimesSelecao.observe(viewLifecycleOwner) { times ->
            if (times.isNullOrEmpty()) return@observe
            val nomesDosTimes = times.map { it.nomePopular }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, nomesDosTimes)
            (binding.inputLayoutAdversario.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        }

        (binding.inputLayoutAdversario.editText as? AutoCompleteTextView)?.setOnItemClickListener { _, _, position, _ ->
            val adapter = (binding.inputLayoutAdversario.editText as? AutoCompleteTextView)?.adapter
            val nomeSelecionado = adapter?.getItem(position) as String
            timeAdversarioSelecionado = viewModel.listaTimesSelecao.value?.find { it.nomePopular == nomeSelecionado }
            timeAdversarioSelecionado?.let {
                viewModel.buscarEscudoAdversario(it)
            }
        }

        binding.buttonSelecionarData.setOnClickListener { mostrarDatePicker() }
        binding.buttonSalvarJogo.setOnClickListener { salvarJogo() }
    }


    private fun observeViewModel() {
        viewModel.isLoadingEscudo.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarEscudo.isVisible = isLoading
            binding.buttonSalvarJogo.isEnabled = !isLoading
            if (isLoading) binding.imageViewPreviewEscudo.visibility = View.GONE
        }

        viewModel.escudoUrlResult.observe(viewLifecycleOwner) { result ->
            result?.fold(
                onSuccess = { url ->
                    urlEscudoAdversarioAtual = url
                    binding.imageViewPreviewEscudo.load(url) {
                        placeholder(R.drawable.ic_shield)
                        error(R.drawable.ic_broken_image)
                    }
                    binding.imageViewPreviewEscudo.visibility = View.VISIBLE
                },
                onFailure = { error ->
                    urlEscudoAdversarioAtual = null
                    Toast.makeText(context, "Erro ao buscar escudo.", Toast.LENGTH_LONG).show()
                    binding.imageViewPreviewEscudo.setImageResource(R.drawable.ic_broken_image)
                    binding.imageViewPreviewEscudo.visibility = View.VISIBLE
                }
            )
        }

        viewModel.eventoSalvo.observe(viewLifecycleOwner) { foiSalvo ->
            if (foiSalvo) {
                val message = if (args.jogoId == -1L) "Jogo adicionado!" else "Jogo atualizado!"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
                viewModel.onEventoSalvoCompleto()
            }
        }

        // ✅ LÓGICA ADICIONADA: Observa o jogo para edição e preenche os campos
        viewModel.jogoParaEdicao.observe(viewLifecycleOwner) { jogo ->
            jogo?.let {
                // Preenche os campos com os dados do jogo
                (binding.inputLayoutAdversario.editText as? AutoCompleteTextView)?.setText(it.adversarioNome, false)
                urlEscudoAdversarioAtual = it.adversarioEscudoUrl
                binding.imageViewPreviewEscudo.load(urlEscudoAdversarioAtual) {
                    crossfade(true)
                    placeholder(R.drawable.ic_shield)
                    error(R.drawable.ic_shield)
                }
                binding.imageViewPreviewEscudo.visibility = View.VISIBLE
                dataSelecionada = it.data
                updateDateInView()
            }
        }
    }

    private fun salvarJogo() {
        val nomeAdversario = binding.editTextAdversario.text.toString()
        if (nomeAdversario.isBlank()) {
            Toast.makeText(context, "Por favor, selecione um time adversário.", Toast.LENGTH_SHORT).show()
            return
        }
        if (dataSelecionada == null) {
            Toast.makeText(context, "Por favor, selecione uma data.", Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.salvarJogo(nomeAdversario, urlEscudoAdversarioAtual, dataSelecionada!!)
    }

    private fun mostrarDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Selecione a data do jogo")
            .build()
        datePicker.addOnPositiveButtonClickListener { selection ->
            val timeZone = TimeZone.getDefault()
            val offset = timeZone.getOffset(Date().time) * -1
            dataSelecionada = Date(selection + offset)
            updateDateInView()
        }
        datePicker.show(parentFragmentManager, "DATE_PICKER_TAG")
    }

    private fun updateDateInView() {
        dataSelecionada?.let {
            val formatador = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            binding.textViewDataSelecionada.text = formatador.format(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
