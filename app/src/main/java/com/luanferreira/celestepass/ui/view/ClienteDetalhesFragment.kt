package com.luanferreira.celestepass.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.luanferreira.celestepass.R
import com.luanferreira.celestepass.databinding.FragmentClienteDetalhesBinding
import com.luanferreira.celestepass.ui.viewmodel.ClienteDetalhesViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ClienteDetalhesFragment : Fragment() {
    private var _binding: FragmentClienteDetalhesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ClienteDetalhesViewModel by viewModels()
    private val args: ClienteDetalhesFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentClienteDetalhesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.cliente.observe(viewLifecycleOwner) { cliente ->
            cliente?.let {
                binding.textViewNomeClienteDetalhes.text = it.nome

                // Preenche o campo de CPF
                binding.layoutCpf.iconInfo.setImageResource(R.drawable.ic_badge)
                binding.layoutCpf.textViewInfoTitle.text = "CPF"
                binding.layoutCpf.textViewInfoValue.text = it.cpf.takeIf { !it.isNullOrBlank() } ?: "Não informado"

                // Preenche o campo de Email
                binding.layoutEmail.iconInfo.setImageResource(R.drawable.ic_email)
                binding.layoutEmail.textViewInfoTitle.text = "E-mail"
                binding.layoutEmail.textViewInfoValue.text = it.email.takeIf { !it.isNullOrBlank() } ?: "Não informado"

                // Preenche o campo de Telefone
                binding.layoutTelefone.iconInfo.setImageResource(R.drawable.ic_phone)
                binding.layoutTelefone.textViewInfoTitle.text = "Telefone"
                binding.layoutTelefone.textViewInfoValue.text = it.telefone.takeIf { !it.isNullOrBlank() } ?: "Não informado"

                // Preenche o campo de Data de Nascimento
                binding.layoutDataNascimento.iconInfo.setImageResource(R.drawable.ic_cake)
                binding.layoutDataNascimento.textViewInfoTitle.text = "Data de Nascimento"
                it.dataNascimento?.let { data ->
                    val formatador = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("pt", "BR"))
                    binding.layoutDataNascimento.textViewInfoValue.text = formatador.format(data)
                } ?: run {
                    binding.layoutDataNascimento.textViewInfoValue.text = "Não informada"
                }
            }
        }

        binding.fabEditarCliente.setOnClickListener {
            val action = ClienteDetalhesFragmentDirections.actionClienteDetalhesFragmentToAddClienteFragment(args.clienteId)
            findNavController().navigate(action)
        }
    }
    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
