package com.luanferreira.celestepass.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
    private val args: AddClienteFragmentArgs by navArgs()
    private var dataNascimentoSelecionada: Date? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddClienteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ESTA É A LÓGICA CHAVE QUE ESTAVA EM FALTA
        viewModel.clienteParaEdicao.observe(viewLifecycleOwner) { cliente ->
            // Este bloco só é executado se estivermos a editar (cliente não é nulo)
            cliente?.let {
                // 1. Altera o título da tela na barra de ferramentas
                (activity as? AppCompatActivity)?.supportActionBar?.title = "Editar Cliente"

                // 2. Pré-preenche todos os campos do formulário
                binding.editTextNomeCliente.setText(it.nome)
                binding.editTextCpfCliente.setText(it.cpf)
                binding.editTextEmailCliente.setText(it.email)
                binding.editTextTelefoneCliente.setText(it.telefone)

                it.dataNascimento?.let { data ->
                    dataNascimentoSelecionada = data // Guarda a data para não a perder
                    val formatador = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    binding.textViewDataNascimentoSelecionada.text = formatador.format(data)
                    binding.textViewDataNascimentoSelecionada.visibility = View.VISIBLE
                }
            }
        }

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
                findNavController().popBackStack() // Volta para a tela de detalhes
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
