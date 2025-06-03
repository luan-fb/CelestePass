package com.luanferreira.celestepass.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.luanferreira.celestepass.databinding.FragmentAddGameBinding
import com.luanferreira.celestepass.ui.viewmodel.AddGameViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddGameFragment : Fragment() {

    private var _binding: FragmentAddGameBinding? = null
    private val binding get() = _binding!! // Se _binding for null aqui, o app vai crashar (o que é bom para diagnóstico)

    private val viewModel: AddGameViewModel by viewModels()
    private var dataSelecionada: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // LOG PARA VERIFICAR SE onViewCreated É CHAMADO E SE O BINDING ESTÁ OK


        binding.buttonSelecionarData.setOnClickListener {

            mostrarDatePicker()
        }

        binding.buttonSalvarJogo.setOnClickListener {

            val nomeAdversario = binding.editTextAdversario.text.toString()



            if (nomeAdversario.isBlank()) {

                Toast.makeText(context, "Por favor, insira o nome do adversário.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (dataSelecionada == null) {

                Toast.makeText(context, "Por favor, selecione uma data.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.salvarJogo(nomeAdversario, dataSelecionada!!)


            Toast.makeText(context, "Jogo salvo com sucesso!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
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