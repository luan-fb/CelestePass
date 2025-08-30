package com.luanferreira.celestepass.ui.view


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.luanferreira.celestepass.R
import com.luanferreira.celestepass.data.model.Cliente
import com.luanferreira.celestepass.data.model.Setor
import com.luanferreira.celestepass.ui.viewmodel.ManagementViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ManagementScreen(
    viewModel: ManagementViewModel = hiltViewModel(),
    onAddClienteClick: () -> Unit,
    onAddSetorClick: () -> Unit,
    onClienteClick: (Long) -> Unit,
    onSetorClick: (Long) -> Unit
) {
    val tabTitles = listOf("Clientes", "Setores")
    val pagerState = rememberPagerState { tabTitles.size }
    val coroutineScope = rememberCoroutineScope()

    // Observa os LiveData do ViewModel e converte-os em State do Compose
    val clientes by viewModel.allClientes.observeAsState(initial = emptyList())
    val setores by viewModel.allSetores.observeAsState(initial = emptyList())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                when (pagerState.currentPage) {
                    0 -> onAddClienteClick()
                    1 -> onAddSetorClick()
                }
            }) {
                Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = "Adicionar")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            TabRow(selectedTabIndex = pagerState.currentPage) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch { pagerState.animateScrollToPage(index) }
                        },
                        text = { Text(title) }
                    )
                }
            }

            HorizontalPager(state = pagerState) { page ->
                when (page) {
                    0 -> ClientList(
                        clientes = clientes,
                        onItemClick = { cliente -> onClienteClick(cliente.id) },
                        onDeleteClick = { cliente -> viewModel.deleteCliente(cliente) }
                    )
                    1 -> SectorList(
                        setores = setores,
                        onItemClick = { setor -> onSetorClick(setor.id) },
                        onDeleteClick = { setor -> viewModel.deleteSetor(setor) }
                    )
                }
            }
        }
    }
}
@Composable
fun ClientList(
    clientes: List<Cliente>,
    onItemClick: (Cliente) -> Unit,
    onDeleteClick: (Cliente) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(clientes, key = { it.id }) { cliente ->
            ManagementListItem(
                name = cliente.nome,
                onClick = { onItemClick(cliente) },
                onDelete = { onDeleteClick(cliente) }
            )
        }
    }
}

@Composable
fun SectorList(
    setores: List<Setor>,
    onItemClick: (Setor) -> Unit,
    onDeleteClick: (Setor) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(setores, key = { it.id }) { setor ->
            ManagementListItem(
                name = setor.nome,
                onClick = { onItemClick(setor) },
                onDelete = { onDeleteClick(setor) }
            )
        }
    }
}

@Composable
fun ManagementListItem(
    name: String,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = name, modifier = Modifier.weight(1f))
            IconButton(onClick = onDelete) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Deletar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SectorListPreview() {
    MaterialTheme {
        ManagementListItem(
            name = "Nome do Cliente de Exemplo",
            onClick = {},
            onDelete = {}
        )
    }
}
