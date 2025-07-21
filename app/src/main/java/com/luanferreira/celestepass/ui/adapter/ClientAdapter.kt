package com.luanferreira.celestepass.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.luanferreira.celestepass.data.model.Cliente
import com.luanferreira.celestepass.databinding.ItemManagementBinding

class ClientAdapter(
    private val onDeleteClicked: (Cliente) -> Unit
) : ListAdapter<Cliente, ClientAdapter.ClienteViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val binding = ItemManagementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClienteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        holder.bind(getItem(position), onDeleteClicked)
    }

    class ClienteViewHolder(private val binding: ItemManagementBinding) : RecyclerView.ViewHolder(binding.root) {
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
