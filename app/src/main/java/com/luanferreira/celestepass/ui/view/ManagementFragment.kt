package com.luanferreira.celestepass.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.luanferreira.celestepass.R
import com.luanferreira.celestepass.databinding.FragmentManagementBinding
import com.luanferreira.celestepass.ui.adapter.ManagementPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManagementFragment : Fragment() {
    private var _binding: FragmentManagementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()

        binding.fabManagementAdd.setOnClickListener {
            // Verifica qual aba está selecionada e navega para a tela de cadastro correta
            when (binding.tabLayoutManagement.selectedTabPosition) {
                0 -> findNavController().navigate(R.id.action_managementFragment_to_addClienteFragment)
                1 -> findNavController().navigate(R.id.action_managementFragment_to_addSetorFragment)
            }
        }
    }

    private fun setupViewPager() {
        binding.viewPagerManagement.adapter = ManagementPagerAdapter(this)
        TabLayoutMediator(binding.tabLayoutManagement, binding.viewPagerManagement) { tab, position ->
            tab.text = when (position) {
                0 -> "Clientes"
                1 -> "Setores"
                else -> null
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}