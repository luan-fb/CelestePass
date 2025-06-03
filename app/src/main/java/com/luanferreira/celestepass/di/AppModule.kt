package com.luanferreira.celestepass.di

import android.content.Context
import androidx.room.Room
import com.luanferreira.celestepass.data.local.AppDatabase
import com.luanferreira.celestepass.data.repository.CelestePassRepository
import com.luanferreira.celestepass.data.repository.CelestePassRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "celestepass_db"
        ).build()
    }

    @Provides
    fun provideJogoDao(db: AppDatabase) = db.jogoDao()
    @Provides
    fun provideSetorDao(db: AppDatabase) = db.setorDao()
    @Provides
    fun provideClienteDao(db: AppDatabase) = db.clienteDao()
    @Provides
    fun provideIngressoDao(db: AppDatabase) = db.ingressoDao()
    @Provides
    fun provideVendaDao(db: AppDatabase) = db.vendaDao()

    @Provides
    @Singleton
    fun provideCelestePassRepository(db: AppDatabase): CelestePassRepository {
        return CelestePassRepositoryImpl(
            jogoDao = db.jogoDao(),
            setorDao = db.setorDao(),
            clienteDao = db.clienteDao(),
            ingressoDao = db.ingressoDao(),
            vendaDao = db.vendaDao()
        )
    }
}