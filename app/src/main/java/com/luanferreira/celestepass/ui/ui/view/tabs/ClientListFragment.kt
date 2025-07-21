package com.luanferreira.celestepass.ui.view.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.luanferreira.celestepass.databinding.FragmentListPlaceholderBinding
import com.luanferreira.celestepass.ui.adapter.ClientAdapter
import com.luanferreira.celestepass.ui.viewmodel.ManagementViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClientListFragment : Fragment() {
    private var _binding: FragmentListPlaceholderBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ManagementViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListPlaceholderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val clientAdapter = ClientAdapter { cliente ->
            AlertDialog.Builder(requireContext())
                .setTitle("Deletar Cliente")
                .setMessage("Tem certeza que deseja deletar o cliente '${cliente.nome}'?")
                .setPositiveButton("Deletar") { _, _ -> viewModel.deleteCliente(cliente) }
                .setNegativeButton("Cancelar", null)
                .show()
        }
        binding.recyclerViewList.adapter = clientAdapter
        binding.recyclerViewList.layoutManager = LinearLayoutManager(context)

        viewModel.allClientes.observe(viewLifecycleOwner) { clientes ->
            clientAdapter.submitList(clientes)
        }
    }
    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
