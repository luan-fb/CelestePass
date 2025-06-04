package com.luanferreira.celestepass.data.model // Ou seu pacote escolhido

import com.google.gson.annotations.SerializedName

data class TimeCrestResponse(
    @SerializedName("crest")
    val crestUrl: String? // A URL do escudo, pode ser nula
)
