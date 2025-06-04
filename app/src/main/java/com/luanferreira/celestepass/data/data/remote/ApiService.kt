package com.luanferreira.celestepass.data.remote

import com.luanferreira.celestepass.data.model.TimeCrestResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiService {

    @GET("teams/{id}") // Endpoint para buscar detalhes de um time específico
    suspend fun getTeamDetails(
        @Header("X-Auth-Token") authToken: String, // Seu token de autenticação
        @Path("id") teamId: Int                    // O ID do time a ser buscado
    ): Response<TimeCrestResponse> // Esperamos uma resposta contendo a URL do escudo

}

// Constantes para a API
const val API_BASE_URL = "https://api.football-data.org/v4/"
const val FOOTBALL_DATA_AUTH_TOKEN = "9bd381d666ba42478a0978669a364774"