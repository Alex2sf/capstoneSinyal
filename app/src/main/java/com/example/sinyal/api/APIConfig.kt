package com.example.sinyal.api

import com.example.sinyal.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIConfig {
    companion object {
        fun getApiService(): APIService {
            val loggingInterceptor =
                if (BuildConfig.DEBUG)
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                else HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)

            val headerInterceptor = object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request: Request = chain.request()
                        .newBuilder()
                        .addHeader("Accept", "application/json")
                        .build()
                    return chain.proceed(request)
                }
            }

            // Konfigurasi Gson dengan setelan lenient
            val gson = GsonBuilder()
                .setLenient()
                .create()

            val client = OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)) // Menggunakan Gson yang telah dikonfigurasi
                .client(client)
                .build()

            return retrofit.create(APIService::class.java)
        }
    }
}