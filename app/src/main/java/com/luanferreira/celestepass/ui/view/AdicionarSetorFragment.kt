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
import com.luanferreira.celestepass.databinding.FragmentAddSetorBinding
import com.luanferreira.celestepass.ui.viewmodel.AdicionarSetorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdicionarSetorFragment : Fragment() {
    private var _binding: FragmentAddSetorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdicionarSetorViewModel by viewModels()
    private val args: AddSetorFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddSetorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observa os dados do setor para preencher o formulário em modo de edição
        viewModel.setorParaEdicao.observe(viewLifecycleOwner) { setor ->
            setor?.let {
                // ✅ PRÉ-PREENCHE O CAMPO
                binding.editTextNomeSetor.setText(it.nome)
                // ✅ ALTERA O TÍTULO DA TELA
                (activity as? AppCompatActivity)?.supportActionBar?.title = "Editar Setor"
            }
        }

        binding.buttonSalvarSetor.setOnClickListener {
            val nomeSetor = binding.editTextNomeSetor.text.toString()
            viewModel.salvarSetor(nomeSetor)
        }

        viewModel.setorSalvoEvento.observe(viewLifecycleOwner) { foiSalvo ->
            if (foiSalvo == true) {
                Toast.makeText(context, "Setor salvo com sucesso!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
                viewModel.onSetorSalvoEventoCompleto()
            }
        }
    }
    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
