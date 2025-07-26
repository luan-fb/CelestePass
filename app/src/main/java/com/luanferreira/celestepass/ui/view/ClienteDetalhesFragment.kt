package com.luanferreira.celestepass.ui.view



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
                binding.textViewCpfDetalhes.text = it.cpf.takeIf { !it.isNullOrBlank() } ?: "Não informado"
                binding.textViewEmailDetalhes.text = it.email.takeIf { !it.isNullOrBlank() } ?: "Não informado"
                binding.textViewTelefoneDetalhes.text = it.telefone.takeIf { !it.isNullOrBlank() } ?: "Não informado"

                it.dataNascimento?.let { data ->
                    val formatador = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    binding.textViewDataNascimentoDetalhes.text = formatador.format(data)
                } ?: run {
                    binding.textViewDataNascimentoDetalhes.text = "Não informada"
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
