package com.luanferreira.celestepass.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.luanferreira.celestepass.databinding.FragmentAddClienteBinding
import com.luanferreira.celestepass.ui.viewmodel.AddClienteViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddClienteFragment : Fragment() {
    private var _binding: FragmentAddClienteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddClienteViewModel by viewModels()
    private var dataNascimentoSelecionada: Date? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddClienteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSelecionarDataNascimento.setOnClickListener {
            mostrarDatePicker()
        }

        binding.buttonSalvarCliente.setOnClickListener {
            val nome = binding.editTextNomeCliente.text.toString()
            val cpf = binding.editTextCpfCliente.text.toString().takeIf { it.isNotBlank() }
            val email = binding.editTextEmailCliente.text.toString().takeIf { it.isNotBlank() }
            val telefone = binding.editTextTelefoneCliente.text.toString().takeIf { it.isNotBlank() }
            viewModel.salvarCliente(nome, cpf, email, telefone, dataNascimentoSelecionada)
        }

        viewModel.clienteSalvoEvento.observe(viewLifecycleOwner) { foiSalvo ->
            if (foiSalvo == true) {
                Toast.makeText(context, "Cliente salvo com sucesso!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
                viewModel.onClienteSalvoEventoCompleto()
            }
        }
    }

    private fun mostrarDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Selecione a data de nascimento")
            .build()
        datePicker.addOnPositiveButtonClickListener { selection ->
            val timeZone = TimeZone.getDefault()
            val offset = timeZone.getOffset(Date().time) * -1
            dataNascimentoSelecionada = Date(selection + offset)
            val formatador = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            binding.textViewDataNascimentoSelecionada.text = formatador.format(dataNascimentoSelecionada!!)
            binding.textViewDataNascimentoSelecionada.visibility = View.VISIBLE
        }
        datePicker.show(parentFragmentManager, "DATE_PICKER_TAG")
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
