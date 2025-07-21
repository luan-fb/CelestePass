package com.luanferreira.celestepass.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.luanferreira.celestepass.R
import com.luanferreira.celestepass.data.model.Ingresso
import com.luanferreira.celestepass.data.model.Setor
import java.text.NumberFormat
import java.util.Locale

// Data class para combinar Ingresso e Setor para exibição
data class IngressoComSetor(
    val ingresso: Ingresso,
    val setor: Setor?
)

class IngressoAdapter : ListAdapter<IngressoComSetor, IngressoAdapter.IngressoViewHolder>(IngressoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngressoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingresso_comprado, parent, false)
        return IngressoViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngressoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class IngressoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val quantidadeSetorTextView: TextView = itemView.findViewById(R.id.text_view_quantidade_setor)
        private val valorCompraTextView: TextView = itemView.findViewById(R.id.text_view_valor_compra_item)

        fun bind(ingressoComSetor: IngressoComSetor) {
            val ingresso = ingressoComSetor.ingresso
            val setorNome = ingressoComSetor.setor?.nome ?: "Setor Desconhecido"

            quantidadeSetorTextView.text = "${ingresso.quantidade}x - $setorNome"

            val formatoMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
            val valorFormatado = formatoMoeda.format(ingresso.valorCompra)
            valorCompraTextView.text = "Comprado por: $valorFormatado cada"
        }
    }

    class IngressoDiffCallback : DiffUtil.ItemCallback<IngressoComSetor>() {
        override fun areItemsTheSame(oldItem: IngressoComSetor, newItem: IngressoComSetor): Boolean {
            return oldItem.ingresso.id == newItem.ingresso.id
        }

        override fun areContentsTheSame(oldItem: IngressoComSetor, newItem: IngressoComSetor): Boolean {
            return oldItem == newItem
        }
    }
}