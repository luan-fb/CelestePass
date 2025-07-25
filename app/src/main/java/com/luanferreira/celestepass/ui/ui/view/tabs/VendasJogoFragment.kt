package com.luanferreira.celestepass.ui.view.tabs

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luanferreira.celestepass.R
import com.luanferreira.celestepass.databinding.FragmentVendasJogoBinding
import com.luanferreira.celestepass.ui.adapter.VendaAdapter
import com.luanferreira.celestepass.ui.viewmodel.DetalhesJogoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VendasJogoFragment : Fragment() {

    private var _binding: FragmentVendasJogoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetalhesJogoViewModel by viewModels({ requireParentFragment() })
    private val vendaAdapter = VendaAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVendasJogoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewVendas.apply {
            adapter = vendaAdapter
            layoutManager = LinearLayoutManager(context)
        }
        setupSwipeToDelete()
    }

    private fun setupSwipeToDelete() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val vendaParaDeletar = vendaAdapter.currentList[position]

                AlertDialog.Builder(requireContext())
                    .setTitle("Deletar Venda")
                    .setMessage("Tem certeza que deseja deletar esta venda? A quantidade de ingressos será devolvida ao lote original.")
                    .setPositiveButton("Deletar") { _, _ ->
                        viewModel.deleteVenda(vendaParaDeletar,position)
                    }
                    .setNegativeButton("Cancelar") { _, _ ->
                        vendaAdapter.notifyItemChanged(position)
                    }
                    .setCancelable(false)
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
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.recyclerViewVendas)
    }

    private fun observeViewModel() {
        viewModel.vendasDetalhadas.observe(viewLifecycleOwner) { listaVendas ->
            vendaAdapter.submitList(listaVendas)
        }
        viewModel.eventoVendaNaoRemovida.observe(viewLifecycleOwner) { posicao ->
            AlertDialog.Builder(requireContext())
                .setTitle("Venda já entregue")
                .setMessage("Essa venda já foi marcada como entregue e não pode ser removida.")
                .setPositiveButton("Ok") { _, _ ->
                    vendaAdapter.notifyItemChanged(posicao) // 🔄 Restaura o item visualmente
                }
                .setCancelable(false)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(jogoId: Long) =
            VendasJogoFragment().apply {
                arguments = Bundle().apply {
                    putLong("jogo_id", jogoId)
                }
            }
    }
}