package com.luanferreira.celestepass.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.luanferreira.celestepass.R
import com.luanferreira.celestepass.ui.view.ManagementScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManagementFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent{
                MaterialTheme {
                    ManagementScreen(
                        onAddClienteClick = {
                            findNavController().navigate(R.id.action_managementFragment_to_addClienteFragment)
                        },
                        onAddSetorClick = {
                            findNavController().navigate(R.id.action_managementFragment_to_addSetorFragment)
                        },
                        onClienteClick = { clienteId ->
                            val action = ManagementFragmentDirections.actionManagementFragmentToClienteDetalhesFragment(clienteId)
                            findNavController().navigate(action)
                        },
                        onSetorClick = { setorId ->
                            val action = ManagementFragmentDirections.actionManagementFragmentToAddSetorFragment(setorId)
                            findNavController().navigate(action)
                        }
                    )
                }
            }
        }
    }
}