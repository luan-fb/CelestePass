package com.luanferreira.celestepass.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.luanferreira.celestepass.data.local.Converters
import com.luanferreira.celestepass.data.model.*

@Database(
    entities = [Jogo::class, Setor::class, Cliente::class, Ingresso::class, Venda::class],
    version = 2 // Incremente a versão se mudar a estrutura das tabelas
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun jogoDao(): JogoDao
    abstract fun setorDao(): SetorDao
    abstract fun clienteDao(): ClienteDao
    abstract fun ingressoDao(): IngressoDao
    abstract fun vendaDao(): VendaDao
}