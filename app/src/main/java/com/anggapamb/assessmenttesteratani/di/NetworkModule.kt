package com.anggapamb.assessmenttesteratani.di

import com.anggapamb.assessmenttesteratani.BuildConfig
import com.anggapamb.assessmenttesteratani.data.source.remote.ApiService
import com.nuvyz.core.data.remote.SSLTrust
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())

        return retrofit.build().create(ApiService::class.java)
    }


    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {

        val okHttpClientBuilder = SSLTrust.createUnsafeOkHttpClient()

        return okHttpClientBuilder
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor {
                val original = it.request()
                val requestBuilder = original.newBuilder()
                    .header(AUTHORIZATION, "Bearer ${BuildConfig.token}")
                    .header(ACCEPT, "application/json")
                    .header("Content-Type", "application/json")

                it.proceed(requestBuilder.build())
            }
            .build()
    }

    companion object {
        const val AUTHORIZATION = "Authorization"
        const val ACCEPT = "Accept"
    }
}