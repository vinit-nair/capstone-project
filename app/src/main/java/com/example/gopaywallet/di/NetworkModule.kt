package com.example.gopaywallet.di

import android.annotation.SuppressLint
import com.example.gopaywallet.data.api.AuthApi
import com.example.gopaywallet.data.api.TransactionApi
import com.example.gopaywallet.utils.LocalDateTimeAdapter
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

object NetworkModule {
    private const val BASE_URL = "http://10.0.2.2:8080/" // Replace with your computer's IP address

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
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
    val transactionApi: TransactionApi = retrofit.create(TransactionApi::class.java)
} 