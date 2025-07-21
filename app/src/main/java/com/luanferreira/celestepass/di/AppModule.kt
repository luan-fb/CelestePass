package com.luanferreira.celestepass.di

import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.luanferreira.celestepass.data.local.AppDatabase
import com.luanferreira.celestepass.data.remote.API_BASE_URL
import com.luanferreira.celestepass.data.remote.ApiService
import com.luanferreira.celestepass.data.repository.CelestePassRepository
import com.luanferreira.celestepass.data.repository.CelestePassRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
        ).fallbackToDestructiveMigration().build()
    }

    // DAOs
    @Provides fun provideJogoDao(db: AppDatabase) = db.jogoDao()
    @Provides fun provideSetorDao(db: AppDatabase) = db.setorDao()
    @Provides fun provideClienteDao(db: AppDatabase) = db.clienteDao()
    @Provides fun provideIngressoDao(db: AppDatabase) = db.ingressoDao()
    @Provides fun provideVendaDao(db: AppDatabase) = db.vendaDao()

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Loga o corpo das requisições/respostas
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Adiciona o interceptor para logs da API
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL) // Usa a constante da URL base
            .client(okHttpClient) // Usa o OkHttpClient configurado
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCelestePassRepository(
        db: AppDatabase,
        apiService: ApiService // Injeta a ApiService
    ): CelestePassRepository {
        return CelestePassRepositoryImpl(
            jogoDao = db.jogoDao(),
            setorDao = db.setorDao(),
            clienteDao = db.clienteDao(),
            ingressoDao = db.ingressoDao(),
            vendaDao = db.vendaDao(),
            apiService = apiService // Passa a ApiService para a implementação
        )
    }
}
