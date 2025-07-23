package com.luanferreira.celestepass.ui.adapter

import android.content.res.ColorStateList
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.luanferreira.celestepass.R
import com.luanferreira.celestepass.databinding.ItemEntregaPendenteBinding

class EntregaAdapter(
    private val onMarcarEntregueClicked: (VendaDetalhada) -> Unit
) : ListAdapter<VendaDetalhada, EntregaAdapter.EntregaViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntregaViewHolder {
        val binding = ItemEntregaPendenteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EntregaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EntregaViewHolder, position: Int) {
        holder.bind(getItem(position), onMarcarEntregueClicked)
    }

    class EntregaViewHolder(private val binding: ItemEntregaPendenteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(vendaDetalhada: VendaDetalhada, onMarcarEntregueClicked: (VendaDetalhada) -> Unit) {
            binding.textViewClienteInfoEntrega.text = "Entregar para: ${vendaDetalhada.cliente?.nome ?: "N/A"}"
            binding.textViewIngressoInfoEntrega.text = "${vendaDetalhada.venda.quantidadeVendida}x - ${vendaDetalhada.setor?.nome ?: "N/A"}"

            if (vendaDetalhada.venda.entregue) {
                // ✅ ESTADO ENTREGUE
                binding.buttonMarcarEntregue.text = "Entregue"
                binding.buttonMarcarEntregue.isEnabled = false

                // Define a cor do ícone para verde
                val successColor = ContextCompat.getColor(itemView.context, R.color.success_green)
                binding.buttonMarcarEntregue.setIconTint(ColorStateList.valueOf(successColor)) // ✅ CORREÇÃO

                // Muda a cor de fundo do card para um cinzento claro
                val backgroundColor = ContextCompat.getColor(itemView.context, R.color.light_gray_background)
                binding.cardViewEntrega.setCardBackgroundColor(backgroundColor)

            } else {
                // ✅ ESTADO PENDENTE
                binding.buttonMarcarEntregue.text = "Marcar como Entregue"
                binding.buttonMarcarEntregue.isEnabled = true
                binding.buttonMarcarEntregue.setIconTint(null) // ✅ CORREÇÃO: Reseta para a cor padrão do tema

                // Garante que a cor de fundo volta ao normal (cor de superfície do tema)
                val typedValue = TypedValue()
                itemView.context.theme.resolveAttribute(com.google.android.material.R.attr.colorSurface, typedValue, true)
                binding.cardViewEntrega.setCardBackgroundColor(typedValue.data)

                binding.buttonMarcarEntregue.setOnClickListener {
                    onMarcarEntregueClicked(vendaDetalhada)
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<VendaDetalhada>() {
        override fun areItemsTheSame(old: VendaDetalhada, new: VendaDetalhada) = old.venda.id == new.venda.id
        override fun areContentsTheSame(old: VendaDetalhada, new: VendaDetalhada) = old == new
    }
}
