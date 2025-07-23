package com.luanferreira.celestepass.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.luanferreira.celestepass.databinding.ItemVendaBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class VendaAdapter : ListAdapter<VendaDetalhada, VendaAdapter.VendaViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendaViewHolder {
        val binding = ItemVendaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VendaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VendaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class VendaViewHolder(private val binding: ItemVendaBinding) : RecyclerView.ViewHolder(binding.root) {
        private val formatoMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        private val formatoData = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        fun bind(vendaDetalhada: VendaDetalhada) {
            val venda = vendaDetalhada.venda

            binding.textViewClienteInfo.text = "Vendido para: ${vendaDetalhada.cliente?.nome ?: "Cliente não encontrado"}"
            binding.textViewIngressoInfo.text = "${venda.quantidadeVendida}x - ${vendaDetalhada.setor?.nome ?: "Setor não encontrado"}"
            binding.textViewDataVenda.text = "Vendido em: ${formatoData.format(venda.dataVenda)}"

            val valorTotal = venda.quantidadeVendida * venda.valorVendaUnitario
            binding.textViewValorTotalVenda.text = formatoMoeda.format(valorTotal)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<VendaDetalhada>() {
        override fun areItemsTheSame(oldItem: VendaDetalhada, newItem: VendaDetalhada): Boolean {
            return oldItem.venda.id == newItem.venda.id
        }
        override fun areContentsTheSame(oldItem: VendaDetalhada, newItem: VendaDetalhada): Boolean {
            return oldItem == newItem
        }
    }
}
