package com.luanferreira.celestepass.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clientes")
data class Cliente (
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val nome : String,
    val telefone: String){

}
