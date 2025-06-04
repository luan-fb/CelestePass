package com.luanferreira.celestepass.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.luanferreira.celestepass.R
import com.luanferreira.celestepass.data.model.Jogo
import java.text.SimpleDateFormat
import java.util.*

class JogoAdapter : ListAdapter<Jogo, JogoAdapter.JogoViewHolder>(JogoDiffCallback()) {

    var onItemClickListener: ((Jogo) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JogoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_jogo, parent, false)
        return JogoViewHolder(view)
    }

    override fun onBindViewHolder(holder: JogoViewHolder, position: Int) {
        val jogo = getItem(position)
        holder.bind(jogo)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(jogo)
        }
    }

    class JogoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val adversarioNomeTextView: TextView = itemView.findViewById(R.id.text_view_adversario_nome)
        private val dataTextView: TextView = itemView.findViewById(R.id.text_view_data)
        private val escudoAdversarioImageView: ImageView = itemView.findViewById(R.id.image_view_escudo_adversario)
        private val escudoCruzeiroImageView: ImageView = itemView.findViewById(R.id.image_view_escudo_cruzeiro)
        private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        fun bind(jogo: Jogo) {
            adversarioNomeTextView.text = jogo.adversarioNome
            dataTextView.text = dateFormatter.format(jogo.data)

            escudoCruzeiroImageView.setImageResource(R.drawable.ic_cruzeiro_logo_placeholder)

            if (!jogo.adversarioEscudoUrl.isNullOrEmpty()) {
                escudoAdversarioImageView.load(jogo.adversarioEscudoUrl) {
                    placeholder(R.drawable.ic_shield)
                    error(R.drawable.ic_broken_image)
                }
            } else {
                escudoAdversarioImageView.setImageResource(R.drawable.ic_shield)
            }
        }
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
