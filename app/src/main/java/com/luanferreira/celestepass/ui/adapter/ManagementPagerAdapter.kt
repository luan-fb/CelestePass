package com.luanferreira.celestepass.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.luanferreira.celestepass.ui.view.tabs.ClienteListaFragment
import com.luanferreira.celestepass.ui.view.tabs.SetorListaFragment

class ManagementPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2 // Duas abas: Clientes e Setores

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ClienteListaFragment()
            1 -> SetorListaFragment()
            else -> throw IllegalStateException("Posição de aba inválida")
        }
    }
}
