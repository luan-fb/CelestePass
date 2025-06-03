package com.luanferreira.celestepass.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.luanferreira.celestepass.R
import com.luanferreira.celestepass.data.model.Jogo
import java.text.SimpleDateFormat
import java.util.*

class JogoAdapter : ListAdapter<Jogo, JogoAdapter.JogoViewHolder>(JogoDiffCallback()) {

    class JogoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val adversarioTextView: TextView = view.findViewById(R.id.text_view_adversario)
        private val dataTextView: TextView = view.findViewById(R.id.text_view_data)
        private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        fun bind(jogo: Jogo) {
            adversarioTextView.text = "Cruzeiro vs. ${jogo.adversario}"
            dataTextView.text = dateFormatter.format(jogo.data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JogoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_jogo, parent, false)
        return JogoViewHolder(view)
    }

    override fun onBindViewHolder(holder: JogoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class JogoDiffCallback : DiffUtil.ItemCallback<Jogo>() {
    override fun areItemsTheSame(oldItem: Jogo, newItem: Jogo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Jogo, newItem: Jogo): Boolean {
        return oldItem == newItem
    }
}