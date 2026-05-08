package com.turkcell.data.di
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.turkcell.core.domain.AuthRepository
import com.turkcell.data.remote.AuthApi
import com.turkcell.data.repository.AuthRepositoryImpl
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.dsl.module
import retrofit2.Retrofit


val networkModule = module {
    single { Json { ignoreUnknownKeys = true; isLenient = true } }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.senin-urlin.com/") // Buraya gerçek URL gelmeli
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single { get<Retrofit>().create(AuthApi::class.java) }
}

val repositoryModule = module {
    // AuthRepository (Core'dan) istendiğinde AuthRepositoryImpl (Data'dan) ver
    single<AuthRepository> { AuthRepositoryImpl(get()) }
}