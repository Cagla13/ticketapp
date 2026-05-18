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
import com.turkcell.data.remote.EventApi

val networkModule = module {
    single { Json { ignoreUnknownKeys = true; isLenient = true } }

    single { TokenStore(get()) }
    single { AuthInterceptor(get()) }
    single { TokenAuthenticator(get(), authApiProvider = { get<AuthApi>() }) }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<AuthInterceptor>())
            .authenticator(get<TokenAuthenticator>())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://tickets-api.halitkalayci.com/") // Gerçek Swagger URL'i yazıldı
            .client(get<OkHttpClient>())
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single { get<Retrofit>().create(AuthApi::class.java) }

    // YENİ EKLEDİĞİMİZ SATIR: EventApi'yi Koin'e tanıtıyoruz
    single { get<Retrofit>().create(EventApi::class.java) }
}

val repositoryModule = module {
    // Koin sırasıyla get() kullanarak hem AuthApi'yi hem de TokenStore'u içeriye enjekte eder.
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
}