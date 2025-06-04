package com.luanferreira.celestepass.data

// Data class para representar um time na lista de seleção
data class TimeSelecao(
    val idApi: Int,         // ID do time na API football-data.org
    val nomePopular: String // Nome como será exibido para o usuário
)

object TimesPredefinidos {
    val listaDeTimes: List<TimeSelecao> = listOf(
        TimeSelecao(idApi = 1765, nomePopular = "Fluminense"),
        TimeSelecao(idApi = 1766, nomePopular = "Frangas"),
        TimeSelecao(idApi = 1767, nomePopular = "Grêmio "),
        TimeSelecao(idApi = 1769, nomePopular = "SE Porcus"),
        TimeSelecao(idApi = 1770, nomePopular = "Botafogo FR"),
        // O ID 1771 é o Cruzeiro, não precisa estar na lista de adversários
        TimeSelecao(idApi = 1776, nomePopular = "São Paulo FC"),
        TimeSelecao(idApi = 1777, nomePopular = "EC Bagay"),
        TimeSelecao(idApi = 1778, nomePopular = "Sport Clube Recife"),
        TimeSelecao(idApi = 1779, nomePopular = "SC Corinthians Paulista"),
        TimeSelecao(idApi = 1780, nomePopular = "Vasco da Lama"),
        TimeSelecao(idApi = 1782, nomePopular = "EC Vitória"),
        TimeSelecao(idApi = 1783, nomePopular = "CR Flamengo"),
        TimeSelecao(idApi = 1837, nomePopular = "Ceará SC"),
        TimeSelecao(idApi = 3984, nomePopular = "Fortaleza EC"),
        TimeSelecao(idApi = 4245, nomePopular = "EC Juventude"),
        TimeSelecao(idApi = 4286, nomePopular = "RB Bragantino"),
        TimeSelecao(idApi = 4364, nomePopular = "Mirassol FC"),
        TimeSelecao(idApi = 6684, nomePopular = "SC Internacional"),
        TimeSelecao(idApi = 6685, nomePopular = "Santos FC")
    ).sortedBy { it.nomePopular } // Ordena a lista por nome para facilitar a seleção
}
