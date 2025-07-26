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
import com.luanferreira.celestepass.data.model.Venda
import com.luanferreira.celestepass.databinding.ItemEntregaPendenteBinding

class EntregaAdapter(
    private val onActionClicked: (venda: VendaDetalhada, isEntregue: Boolean) -> Unit
) : ListAdapter<VendaDetalhada, EntregaAdapter.EntregaViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntregaViewHolder {
        val binding = ItemEntregaPendenteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EntregaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EntregaViewHolder, position: Int) {
        holder.bind(getItem(position), onActionClicked)
    }

    class EntregaViewHolder(private val binding: ItemEntregaPendenteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(vendaDetalhada: VendaDetalhada, onActionClicked: (VendaDetalhada, Boolean) -> Unit) {
            binding.textViewClienteInfoEntrega.text = "Entregar para: ${vendaDetalhada.cliente?.nome ?: "N/A"}"
            binding.textViewIngressoInfoEntrega.text = "${vendaDetalhada.venda.quantidadeVendida}x - ${vendaDetalhada.setor?.nome ?: "N/A"}"

            val button = binding.buttonMarcarEntregue

            if (vendaDetalhada.venda.entregue) {
                // ESTADO ENTREGUE
                button.text = "Desmarcar"
                button.isEnabled = true
                val successColor = ContextCompat.getColor(itemView.context, R.color.success_green)
                button.iconTint = ColorStateList.valueOf(successColor)

                val backgroundColor = ContextCompat.getColor(itemView.context, R.color.light_gray_background)
                binding.cardViewEntrega.setCardBackgroundColor(backgroundColor)

                button.setOnClickListener {
                    onActionClicked(vendaDetalhada, true) // Ação para desmarcar
                }

            } else {
                // ESTADO PENDENTE
                button.text = "Marcar como Entregue"
                button.isEnabled = true
                button.iconTint = null // Cor padrão do tema

                val typedValue = TypedValue()
                itemView.context.theme.resolveAttribute(com.google.android.material.R.attr.colorSurface, typedValue, true)
                binding.cardViewEntrega.setCardBackgroundColor(typedValue.data)

                button.setOnClickListener {
                    onActionClicked(vendaDetalhada, false) // Ação para marcar
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<VendaDetalhada>() {
        override fun areItemsTheSame(old: VendaDetalhada, new: VendaDetalhada) = old.venda.id == new.venda.id
        override fun areContentsTheSame(old: VendaDetalhada, new: VendaDetalhada) = old == new
    }
}
