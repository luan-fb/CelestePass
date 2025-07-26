package com.luanferreira.celestepass.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.luanferreira.celestepass.data.model.Setor
import com.luanferreira.celestepass.databinding.ItemManagementBinding

class SectorAdapter(
    private val onItemClicked: (Setor) -> Unit,
    private val onDeleteClicked: (Setor) -> Unit
) : ListAdapter<Setor, SectorAdapter.SectorViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: SectorViewHolder, position: Int) {
        val setor = getItem(position)
        holder.itemView.setOnClickListener { onItemClicked(setor) }
        holder.bind(setor, onDeleteClicked)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectorViewHolder {
        val binding = ItemManagementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SectorViewHolder(binding)
    }


    class SectorViewHolder(private val binding: ItemManagementBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(setor: Setor, onDeleteClicked: (Setor) -> Unit) {
            binding.textViewItemName.text = setor.nome
            binding.buttonDeleteItem.setOnClickListener { onDeleteClicked(setor) }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Setor>() {
        override fun areItemsTheSame(oldItem: Setor, newItem: Setor) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Setor, newItem: Setor) = oldItem == newItem
    }
}
