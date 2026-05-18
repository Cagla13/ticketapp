package com.turkcell.data.di

import retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.turkcell.core.domain.AuthRepository
import com.turkcell.data.remote.AuthApi
import com.turkcell.data.repository.AuthRepositoryImpl
import com.turkcell.data.network.AuthInterceptor
import com.turkcell.data.network.TokenAuthenticator
import com.turkcell.data.util.TokenStore
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit

val networkModule = module {
    single { Json { ignoreUnknownKeys = true; isLenient = true } }

    // Cihazda saklama aracı (Context'i Koin kendi bulur)
    single { TokenStore(get()) }

    single { AuthInterceptor(get()) }

    single { TokenAuthenticator(get(), authApiProvider = { get<AuthApi>() }) }

    // OkHttpClient'ı Interceptor ve Authenticator ile besliyoruz
    single {
        OkHttpClient.Builder()
            .addInterceptor(get<AuthInterceptor>())
            .authenticator(get<TokenAuthenticator>())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.senin-urlin.com/") // Buraya gerçek Swagger / API URL'ini yazacağız
            .client(get<OkHttpClient>()) // OkHttpClient bağlandı
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single { get<Retrofit>().create(AuthApi::class.java) }
}

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
}