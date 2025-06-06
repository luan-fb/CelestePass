package com.luanferreira.celestepass.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.luanferreira.celestepass.ui.view.tabs.EntregasJogoFragment
import com.luanferreira.celestepass.ui.view.tabs.IngressosCompradosFragment
import com.luanferreira.celestepass.ui.view.tabs.VendasJogoFragment

class DetalhesJogoPagerAdapter(fragment: Fragment, private val jogoId: Long) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3 // Número de abas

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> VendasJogoFragment.newInstance(jogoId)
            1 -> IngressosCompradosFragment.newInstance(jogoId)
            2 -> EntregasJogoFragment.newInstance(jogoId)
            else -> throw IllegalStateException("Posição de aba inválida")
        }
    }
}
