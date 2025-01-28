package com.example.gopaywallet.di

import android.annotation.SuppressLint
import com.example.gopaywallet.data.api.AuthApi
import com.example.gopaywallet.data.api.TransactionApi
import com.example.gopaywallet.data.repository.TransactionRepository
import com.example.gopaywallet.data.repository.TransactionRepositoryImpl
import com.example.gopaywallet.utils.LocalDateTimeAdapter
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import android.content.Context
import com.example.gopaywallet.data.SessionManager
import com.example.gopaywallet.data.api.RewardsApi
import dagger.hilt.android.qualifiers.ApplicationContext
import com.example.gopaywallet.data.repository.UserRepository
import com.example.gopaywallet.data.repository.UserRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    // 10.0.2.2 is the special IP for localhost on Android Emulator
    private const val BASE_URL = "http://10.0.2.2:8081/"

    @SuppressLint("NewApi")
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val token = original.header("Authorization")
        
        val requestBuilder = original.newBuilder()
            .header("Authorization", if (token?.startsWith("Bearer ") == true) token else "Bearer $token")
            .method(original.method, original.body)

        chain.proceed(requestBuilder.build())
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)  // Reduced timeout
        .readTimeout(30, TimeUnit.SECONDS)     // Reduced timeout
        .writeTimeout(30, TimeUnit.SECONDS)    // Reduced timeout
        .retryOnConnectionFailure(true)       // Enable retries
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTransactionApi(retrofit: Retrofit): TransactionApi {
        return retrofit.create(TransactionApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager {
        return SessionManager(context)
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(
        transactionApi: TransactionApi,
        sessionManager: SessionManager
    ): TransactionRepository {
        return TransactionRepositoryImpl(transactionApi, sessionManager)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        authApi: AuthApi,
        sessionManager: SessionManager
    ): UserRepository {
        return UserRepositoryImpl(authApi, sessionManager)
    }

    val rewardsApi: RewardsApi = provideRetrofit().create(RewardsApi::class.java)
}