package com.luanferreira.celestepass.ui.view.tabs

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luanferreira.celestepass.R
import com.luanferreira.celestepass.databinding.FragmentIngressosCompradosBinding
import com.luanferreira.celestepass.ui.adapter.IngressoAdapter
import com.luanferreira.celestepass.ui.viewmodel.DetalhesJogoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IngressosCompradosFragment : Fragment() {

    private var _binding: FragmentIngressosCompradosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetalhesJogoViewModel by viewModels({ requireParentFragment() })
    private val ingressoAdapter = IngressoAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngressosCompradosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewIngressosComprados.apply {
            adapter = ingressoAdapter
            layoutManager = LinearLayoutManager(context)
        }
        setupSwipeToDelete()
    }

    private fun setupSwipeToDelete() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, // Não nos importamos com arrastar e soltar (drag & drop)
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT // Permitir arrastar para a esquerda e direita
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // Não faz nada
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val ingressoParaDeletar = ingressoAdapter.currentList[position]

                // Mostra um diálogo de confirmação antes de deletar
                AlertDialog.Builder(requireContext())
                    .setTitle("Deletar Lote de Ingressos")
                    .setMessage("Tem certeza que deseja deletar este lote de ingressos? Esta ação não pode ser desfeita.")
                    .setPositiveButton("Deletar") { _, _ ->
                        if (ingressoParaDeletar.ingresso.quantidadeVendida > 0) {
                            Toast.makeText(requireContext(), "Não foi possível excluir. Há vendas vinculadas a este ingresso.", Toast.LENGTH_LONG).show();
                            ingressoAdapter.notifyItemChanged(position)
                            return@setPositiveButton
                        }
                        viewModel.deleteIngresso(ingressoParaDeletar.ingresso)
                    }
                    .setNegativeButton("Cancelar") { _, _ ->
                        // Se o utilizador cancelar, notifica o adapter para redesenhar o item na sua posição original
                        ingressoAdapter.notifyItemChanged(position)
                    }
                    .setCancelable(false) // Impede que o diálogo seja fechado ao clicar fora
                    .show()
            }
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

                val itemView = viewHolder.itemView
                val background = ColorDrawable(ContextCompat.getColor(itemView.context, R.color.delete_red))
                val deleteIcon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_delete)!!

                val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2
                val iconTop = itemView.top + (itemView.height - deleteIcon.intrinsicHeight) / 2
                val iconBottom = iconTop + deleteIcon.intrinsicHeight

                if (dX > 0) { // Arrastando para a direita
                    val iconLeft = itemView.left + iconMargin
                    val iconRight = itemView.left + iconMargin + deleteIcon.intrinsicWidth
                    deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    background.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt(), itemView.bottom)
                } else if (dX < 0) { // Arrastando para a esquerda
                    val iconLeft = itemView.right - iconMargin - deleteIcon.intrinsicWidth
                    val iconRight = itemView.right - iconMargin
                    deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                } else { // Sem arrastar
                    background.setBounds(0, 0, 0, 0)
                }
                background.draw(c)
                deleteIcon.draw(c)
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.recyclerViewIngressosComprados)
      }



    private fun observeViewModel() {
        viewModel.ingressosComSetor.observe(viewLifecycleOwner) { listaIngressos ->
            ingressoAdapter.submitList(listaIngressos)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(jogoId: Long) =
            IngressosCompradosFragment().apply {
                arguments = Bundle().apply {
                    putLong("jogo_id", jogoId)
                }
            }
    }
}
