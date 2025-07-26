package com.luanferreira.celestepass.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.luanferreira.celestepass.data.model.Cliente
import com.luanferreira.celestepass.databinding.ItemManagementBinding

class ClientAdapter(
    private val onItemClicked: (Cliente) -> Unit,    // ✅ Ação para o clique no item
    private val onDeleteClicked: (Cliente) -> Unit // ✅ Ação para o clique no botão de deletar
) : ListAdapter<Cliente, ClientAdapter.ClientViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        val binding = ItemManagementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        val cliente = getItem(position)
        // ✅ Adiciona o listener para o clique no item inteiro
        holder.itemView.setOnClickListener { onItemClicked(cliente) }
        // Passa o listener do botão de deletar
        holder.bind(cliente, onDeleteClicked)
    }

    class ClientViewHolder(private val binding: ItemManagementBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cliente: Cliente, onDeleteClicked: (Cliente) -> Unit) {
            binding.textViewItemName.text = cliente.nome
            binding.buttonDeleteItem.setOnClickListener { onDeleteClicked(cliente) }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Cliente>() {
        override fun areItemsTheSame(oldItem: Cliente, newItem: Cliente) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Cliente, newItem: Cliente) = oldItem == newItem
    }
}
