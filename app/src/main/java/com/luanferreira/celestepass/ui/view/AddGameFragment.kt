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
import coil.load
import com.google.android.material.datepicker.MaterialDatePicker
import com.luanferreira.celestepass.R
import com.luanferreira.celestepass.data.TimeSelecao
import com.luanferreira.celestepass.databinding.FragmentAddGameBinding
import com.luanferreira.celestepass.ui.viewmodel.AddGameViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddGameFragment : Fragment() {

    private var _binding: FragmentAddGameBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddGameViewModel by viewModels()
    private var dataSelecionada: Date? = null
    private var timeAdversarioSelecionado: TimeSelecao? = null
    private var urlEscudoAdversarioAtual: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAutoCompleteTextView()
        observeViewModel()

        binding.buttonSelecionarData.setOnClickListener {
            mostrarDatePicker()
        }

        binding.buttonSalvarJogo.setOnClickListener {
            val nomeAdversarioNoCampo = binding.editTextAdversario.text.toString()

            if (timeAdversarioSelecionado == null || nomeAdversarioNoCampo != timeAdversarioSelecionado?.nomePopular) {
                Toast.makeText(context, "Por favor, selecione um time adversário da lista.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (dataSelecionada == null) {
                Toast.makeText(context, "Por favor, selecione uma data.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Usa o nome do timeAdversarioSelecionado e a urlEscudoAdversarioAtual que foi preenchida pelo observer
            viewModel.salvarJogo(timeAdversarioSelecionado!!.nomePopular, urlEscudoAdversarioAtual, dataSelecionada!!)
        }
    }

    private fun setupAutoCompleteTextView() {
        viewModel.listaTimesSelecao.observe(viewLifecycleOwner) { times ->
            if (times.isNullOrEmpty()) {
                Log.w("AddGameFragment", "Lista de times para seleção está vazia ou nula.")
                return@observe
            }
            val nomesDosTimes = times.map { it.nomePopular }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                nomesDosTimes
            )
            (binding.inputLayoutAdversario.editText as? AutoCompleteTextView)?.setAdapter(adapter)
            (binding.inputLayoutAdversario.editText as? AutoCompleteTextView)?.setOnItemClickListener { _, _, position, _ ->
                timeAdversarioSelecionado = times.find { it.nomePopular == adapter.getItem(position) }

                timeAdversarioSelecionado?.let {
                    Log.d("AddGameFragment", "Time selecionado: ${it.nomePopular}, ID: ${it.idApi}")
                    binding.imageViewPreviewEscudo.setImageResource(0) // Limpa preview anterior
                    urlEscudoAdversarioAtual = null // Limpa URL anterior
                    viewModel.buscarEscudoAdversario(it)
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.isLoadingEscudo.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarEscudo.isVisible = isLoading
            binding.buttonSalvarJogo.isEnabled = !isLoading
            if (isLoading) {
                binding.imageViewPreviewEscudo.visibility = View.GONE
            }
        }

        viewModel.escudoUrlResult.observe(viewLifecycleOwner) { result ->
            result?.fold(
                onSuccess = { url ->
                    urlEscudoAdversarioAtual = url
                    Log.d("AddGameFragment", "URL do escudo recebida: $urlEscudoAdversarioAtual")
                    if (!url.isNullOrEmpty()) {
                        binding.imageViewPreviewEscudo.load(url) {
                            placeholder(R.drawable.ic_shield) // Placeholder
                            error(R.drawable.ic_broken_image)   // Imagem de erro
                        }
                        binding.imageViewPreviewEscudo.visibility = View.VISIBLE
                    } else {
                        binding.imageViewPreviewEscudo.setImageResource(R.drawable.ic_shield) // Placeholder se URL for nula/vazia
                        binding.imageViewPreviewEscudo.visibility = View.VISIBLE
                        Toast.makeText(context, "Escudo não encontrado para o time.", Toast.LENGTH_SHORT).show()
                    }
                },
                onFailure = { error ->
                    urlEscudoAdversarioAtual = null
                    Log.e("AddGameFragment", "Erro ao buscar escudo: ${error.message}")
                    Toast.makeText(context, "Erro ao buscar escudo.", Toast.LENGTH_LONG).show()
                    binding.imageViewPreviewEscudo.setImageResource(R.drawable.ic_broken_image) // Imagem de erro
                    binding.imageViewPreviewEscudo.visibility = View.VISIBLE
                }
            )
        }

        viewModel.jogoSalvoEvento.observe(viewLifecycleOwner) { foiSalvo ->
            if (foiSalvo) {
                Toast.makeText(context, "Jogo salvo com sucesso!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
                viewModel.onJogoSalvoEventoCompleto() // Reseta o evento
            }
        }
    }

    private fun mostrarDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Selecione a data do jogo")
            .build()
        datePicker.addOnPositiveButtonClickListener { selection ->
            val timeZone = TimeZone.getDefault()
            val offset = timeZone.getOffset(Date().time) * -1
            dataSelecionada = Date(selection + offset)
            val formatador = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            binding.textViewDataSelecionada.text = formatador.format(dataSelecionada!!)
        }
        datePicker.show(parentFragmentManager, "DATE_PICKER_TAG")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
